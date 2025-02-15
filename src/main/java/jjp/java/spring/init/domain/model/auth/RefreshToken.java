package jjp.java.spring.init.domain.model.auth;

import java.time.LocalDateTime;

public record RefreshToken(
  int accountId,
  String refreshToken,
  LocalDateTime expiryTime
) {
  public boolean expired(LocalDateTime now) {
    return this.expiryTime.isBefore(now);
  }
}
