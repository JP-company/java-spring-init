package jjp.java.spring.init.domain.model;

import java.time.LocalDateTime;

public record EmailAuthCode(
    String email,
    String authCode,
    LocalDateTime expiryTime
) {

  public boolean auth(String authCode) {
    return this.authCode.equals(authCode);
  }

  public boolean expired(LocalDateTime now) {
    return this.expiryTime.isBefore(now);
  }
}
