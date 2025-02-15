package jjp.java.spring.init.domain.model.auth.email;

import java.time.LocalDateTime;

public record AuthEmail(
  Integer accountId,
  String email,
  String authCode,
  LocalDateTime expiryTime
) {
  public boolean equals(String inputAuthCode) {
    return this.authCode.equals(inputAuthCode);
  }

  public boolean expired(LocalDateTime now) {
    return this.expiryTime.isBefore(now);
  }

  public boolean isNewAccount() {
    return this.accountId == null;
  }
}
