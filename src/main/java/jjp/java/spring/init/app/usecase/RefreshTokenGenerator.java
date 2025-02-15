package jjp.java.spring.init.app.usecase;

import static jjp.java.spring.init.domain.exception.AccountException.AccountErrorKey.EXPIRED_REFRESH_TOKEN;

import java.time.LocalDateTime;
import jjp.java.spring.init.domain.command.RefreshTokenUpsert;
import jjp.java.spring.init.domain.exception.AccountException;
import jjp.java.spring.init.domain.model.auth.RefreshToken;

public record RefreshTokenGenerator(
  RefreshToken refreshToken,
  LocalDateTime now,
  String newRefreshToken,
  int expiryDays
) {
  public RefreshTokenUpsert run() {
    this.validate();
    return new RefreshTokenUpsert(
      this.refreshToken.accountId(),
      this.newRefreshToken,
      this.now.plusDays(expiryDays)
    );
  }

  private void validate() {
    if (this.refreshToken.expired(this.now)) {
      throw new AccountException(EXPIRED_REFRESH_TOKEN);
    }
  }
}
