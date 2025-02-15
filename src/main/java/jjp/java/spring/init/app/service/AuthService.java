package jjp.java.spring.init.app.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import jjp.java.spring.init.app.port.db.IAuthDb;
import jjp.java.spring.init.app.port.email.IEmailService;
import jjp.java.spring.init.app.port.security.IAuthenticationTokenProvider;
import jjp.java.spring.init.app.usecase.AuthEmailValidator;
import jjp.java.spring.init.app.usecase.EmailSendValidator;
import jjp.java.spring.init.app.usecase.RefreshTokenGenerator;
import jjp.java.spring.init.domain.command.EmailAuthCodeUpsert;
import jjp.java.spring.init.domain.command.RefreshTokenUpsert;
import jjp.java.spring.init.domain.exception.AccountException;
import jjp.java.spring.init.domain.exception.AccountException.AccountErrorKey;
import jjp.java.spring.init.domain.model.auth.AuthenticationToken;
import jjp.java.spring.init.domain.model.auth.RefreshToken;
import jjp.java.spring.init.domain.model.auth.email.AuthEmail;
import jjp.java.spring.init.domain.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final IAuthDb authDb;
  private final IEmailService emailService;
  private final IAuthenticationTokenProvider tokenProvider;

  @Transactional
  public void sendEmailAuthCode(
    @NonNull final String email,
    @NonNull final LocalDateTime now
  ) {
    new EmailSendValidator(email).run();

    String authCode = RandomUtil.intNumber(4);
    LocalDateTime expiryTime = now.plusMinutes(5);

    this.authDb.upsertAuthCode(
        new EmailAuthCodeUpsert(email, authCode, expiryTime)
      );
    this.emailService.send(email, "인증 메일", "인증번호: " + authCode);
  }

  public AuthenticationToken authByEmail(
    @NonNull final String email,
    @NonNull final String inputAuthCode,
    @NonNull final LocalDateTime now
  ) {
    AuthEmail authEmail =
      this.authDb.findOneAuthEmailBy(email).orElseThrow(() ->
          new AccountException(AccountErrorKey.WRONG_ACCESS)
        );
    new AuthEmailValidator(authEmail, inputAuthCode, now).run();
    if (authEmail.isNewAccount()) {
      return AuthenticationToken.empty();
    }

    String refreshToken = UUID.randomUUID().toString();
    int expiryDays = 7;
    RefreshTokenUpsert refreshTokenUpsert = new RefreshTokenUpsert(
      authEmail.accountId(),
      refreshToken,
      now.plusDays(expiryDays)
    );

    this.authDb.upsertRefreshToken(refreshTokenUpsert);
    String authToken = this.tokenProvider.generateToken(authEmail.accountId());
    return new AuthenticationToken(authToken, refreshToken);
  }

  public AuthenticationToken refresh(
    @NonNull final String refreshToken,
    @NonNull final LocalDateTime now
  ) {
    RefreshToken currentRefreshToken =
      this.authDb.findOneRefreshTokenBy(refreshToken).orElseThrow(() ->
          new AccountException(AccountErrorKey.WRONG_REFRESH_TOKEN)
        );
    String newRefreshToken = UUID.randomUUID().toString();
    int expiryDays = 30;

    RefreshTokenUpsert refreshTokenUpsert = new RefreshTokenGenerator(
      currentRefreshToken,
      now,
      newRefreshToken,
      expiryDays
    ).run();

    this.authDb.upsertRefreshToken(refreshTokenUpsert);
    String authToken =
      this.tokenProvider.generateToken(currentRefreshToken.accountId());
    return new AuthenticationToken(authToken, newRefreshToken);
  }

  public void logout(int accountId) {
    this.authDb.deleteRefreshToken(accountId);
  }
}
