package jjp.java.spring.init.app.service;

import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.WRONG_ACCESS;
import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.WRONG_REFRESH_TOKEN;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import jjp.java.spring.init.app.port.db.IAccountDb;
import jjp.java.spring.init.app.port.email.IEmailService;
import jjp.java.spring.init.app.port.security.IAuthenticationTokenProvider;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.command.EmailAuthCodeUpsert;
import jjp.java.spring.init.domain.command.LoginRefreshTokenUpsert;
import jjp.java.spring.init.domain.common.exception.AccountException;
import jjp.java.spring.init.domain.model.AccountLogin;
import jjp.java.spring.init.domain.model.AuthenticationToken;
import jjp.java.spring.init.domain.model.EmailAuthCode;
import jjp.java.spring.init.domain.model.EmailLimit;
import jjp.java.spring.init.domain.model.RefreshToken;
import jjp.java.spring.init.domain.usecase.AccountRefreshTokenVerify;
import jjp.java.spring.init.domain.usecase.AccountRegister;
import jjp.java.spring.init.domain.usecase.EmailAuthCodeCreator;
import jjp.java.spring.init.domain.usecase.EmailAuthCodeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final IAccountDb accountDb;

  private final IAuthenticationTokenProvider authenticationTokenProvider;
  private final IEmailService emailService;

  public void sendAuthCode(String email, LocalDateTime now) {
    EmailLimit emailLimit = this.accountDb.findOneEmailLimit(email)
        .orElseGet(() -> this.accountDb.saveEmailLimit(email, 0, now.toLocalDate()));

    String authCode = String.format("%06d", new Random().nextInt(1000000));
    EmailAuthCodeUpsert upsert = new EmailAuthCodeCreator(email, authCode, now.plusMinutes(5))
        .validate(emailLimit, now)
        .toUpsert();

    this.accountDb.upsertAuthCode(upsert);
    if (now.toLocalDate().isEqual(emailLimit.date())) {
      this.accountDb.incrementEmailLimit(email);
    } else {
      this.accountDb.saveEmailLimit(email, 1, now.toLocalDate());
    }
    this.emailService.send(email, "인증 메일", "인증번호: " + authCode);
  }

  public AuthenticationToken start(
      String email,
      String authCode,
      LocalDateTime now
  ) {
    EmailAuthCode emailAuthCode = this.accountDb.findOneEmailAuthCodeBy(email)
        .orElseThrow(() -> new AccountException(WRONG_ACCESS));

    new EmailAuthCodeValidator(emailAuthCode)
        .validate(authCode, now);

    Optional<AccountLogin> optionalAccountLogin = this.accountDb.findOneLoginBy(email);
    if (optionalAccountLogin.isEmpty()) {
      return AuthenticationToken.empty();
    }

    AccountLogin accountLogin = optionalAccountLogin.get();
    String refreshToken = UUID.randomUUID().toString();
    LoginRefreshTokenUpsert refreshTokenUpsert =
        new LoginRefreshTokenUpsert(accountLogin.id(), refreshToken, now.plusDays(7));

    this.accountDb.upsertRefreshToken(refreshTokenUpsert);
    String authToken = this.authenticationTokenProvider.generateToken(accountLogin.id());
    return new AuthenticationToken(authToken, refreshToken);
  }

  @Transactional
  public AuthenticationToken register(
      String email, String authCode, String nickname, LocalDateTime now
  ) {
    boolean emailAuthCodeVerified = this.accountDb.existsByEmailAndAuthCode(email, authCode);
    boolean existsByEmail = this.accountDb.existsBy(email);

    AccountInsert accountInsert = new AccountRegister(email, nickname, now)
        .validate(emailAuthCodeVerified, existsByEmail)
        .toInsert();

    int accountId = this.accountDb.insert(accountInsert);
    String refreshToken = UUID.randomUUID().toString();
    LoginRefreshTokenUpsert refreshTokenUpsert =
        new LoginRefreshTokenUpsert(accountId, refreshToken, now.plusDays(7));

    this.accountDb.upsertRefreshToken(refreshTokenUpsert);
    String authToken = this.authenticationTokenProvider.generateToken(accountId);
    return new AuthenticationToken(authToken, refreshToken);
  }

  public AuthenticationToken refresh(String refreshToken, LocalDateTime now) {
    RefreshToken currentRefreshToken = this.accountDb.findOneRefreshTokenBy(refreshToken)
        .orElseThrow(() -> new AccountException(WRONG_REFRESH_TOKEN));

    String newRefreshToken = UUID.randomUUID().toString();
    LoginRefreshTokenUpsert refreshTokenUpsert =
        new AccountRefreshTokenVerify(
            currentRefreshToken.accountId(),
            newRefreshToken,
            now.plusDays(7)
        )
            .validate(currentRefreshToken.expiryTime(), now)
            .toRefreshTokenUpsert();

    this.accountDb.upsertRefreshToken(refreshTokenUpsert);
    String authToken =
        this.authenticationTokenProvider.generateToken(currentRefreshToken.accountId());
    return new AuthenticationToken(authToken, newRefreshToken);
  }

  public void logout(int accountId) {
    this.accountDb.deleteRefreshToken(accountId);
  }
}
