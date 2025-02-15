package jjp.java.spring.init.app.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import jjp.java.spring.init.app.port.db.IAccountDb;
import jjp.java.spring.init.app.port.db.IAuthDb;
import jjp.java.spring.init.app.port.security.IAuthenticationTokenProvider;
import jjp.java.spring.init.app.usecase.AccountRegister;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.command.RefreshTokenUpsert;
import jjp.java.spring.init.domain.exception.AccountException;
import jjp.java.spring.init.domain.exception.AccountException.AccountErrorKey;
import jjp.java.spring.init.domain.model.auth.AuthenticationToken;
import jjp.java.spring.init.domain.model.auth.email.AuthEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final IAuthDb authDb;
  private final IAccountDb accountDb;
  private final IAuthenticationTokenProvider tokenProvider;

  @Transactional
  public AuthenticationToken registerEmailAccount(
    @NonNull final String email,
    @NonNull final String authCode,
    @NonNull final String nickname,
    @NonNull final LocalDateTime now
  ) {
    AuthEmail authEmail =
      this.authDb.findOneAuthEmailBy(email).orElseThrow(() ->
          new AccountException(AccountErrorKey.WRONG_ACCESS)
        );
    if (!authEmail.equals(authCode)) {
      throw new AccountException(AccountErrorKey.INCORRECT_EMAIL_AUTH_CODE);
    }

    AccountInsert accountInsert = new AccountRegister(
      authEmail,
      nickname,
      now
    ).run();
    int accountId = this.accountDb.insert(accountInsert);

    String refreshToken = UUID.randomUUID().toString();
    RefreshTokenUpsert refreshTokenUpsert = new RefreshTokenUpsert(
      accountId,
      refreshToken,
      now.plusDays(7)
    );
    this.authDb.upsertRefreshToken(refreshTokenUpsert);
    this.authDb.updateAuthEmail(email, accountId);

    String authToken = this.tokenProvider.generateToken(accountId);
    return new AuthenticationToken(authToken, refreshToken);
  }
}
