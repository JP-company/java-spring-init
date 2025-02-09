package jjp.java.spring.init.domain.model;

import ch.qos.logback.core.util.StringUtil;

public record AuthenticationToken(String authToken, String refreshToken) {
  public static AuthenticationToken empty() {
    return new AuthenticationToken("", "");
  }

  public boolean isEmpty() {
    return (
      StringUtil.isNullOrEmpty(this.authToken) ||
      StringUtil.isNullOrEmpty(this.refreshToken)
    );
  }
}
