package jjp.java.spring.init.app.usecase;

import static jjp.java.spring.init.domain.exception.AccountException.AccountErrorKey.INCORRECT_PASSWORD;

import java.time.LocalDateTime;
import jjp.java.spring.init.domain.command.RefreshTokenUpsert;
import jjp.java.spring.init.domain.exception.AccountException;

public record AccountLoginVerify(
  int accountId,
  String refreshToken,
  LocalDateTime expiryTime
) {
  public AccountLoginVerify validate(boolean passwordMatches) {
    if (!passwordMatches) {
      throw new AccountException(INCORRECT_PASSWORD);
    }
    return this;
  }

  public RefreshTokenUpsert toRefreshTokenUpsert() {
    return new RefreshTokenUpsert(
      this.accountId,
      this.refreshToken,
      this.expiryTime
    );
  }
}
