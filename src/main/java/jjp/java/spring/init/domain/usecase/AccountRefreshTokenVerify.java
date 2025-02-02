package jjp.java.spring.init.domain.usecase;

import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.EXPIRED_REFRESH_TOKEN;

import java.time.LocalDateTime;
import jjp.java.spring.init.domain.command.LoginRefreshTokenUpsert;
import jjp.java.spring.init.domain.common.exception.AccountException;

public record AccountRefreshTokenVerify(
    int accountId,
    String newRefreshToken,
    LocalDateTime newExpiryTime
) {

  public AccountRefreshTokenVerify validate(LocalDateTime currentExpiryTime, LocalDateTime now) {
    if (currentExpiryTime.isBefore(now)) {
      throw new AccountException(EXPIRED_REFRESH_TOKEN);
    }
    return this;
  }

  public LoginRefreshTokenUpsert toRefreshTokenUpsert() {
    return new LoginRefreshTokenUpsert(
        this.accountId,
        this.newRefreshToken,
        this.newExpiryTime
    );
  }
}
