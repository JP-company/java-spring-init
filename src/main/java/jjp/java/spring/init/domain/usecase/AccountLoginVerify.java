package jjp.java.spring.init.domain.usecase;

import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.INCORRECT_PASSWORD;

import java.time.LocalDateTime;
import jjp.java.spring.init.domain.command.LoginRefreshTokenUpsert;
import jjp.java.spring.init.domain.common.exception.AccountException;

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

  public LoginRefreshTokenUpsert toRefreshTokenUpsert() {
    return new LoginRefreshTokenUpsert(this.accountId, this.refreshToken, this.expiryTime);
  }
}
