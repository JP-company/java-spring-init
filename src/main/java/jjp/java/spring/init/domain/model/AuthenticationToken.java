package jjp.java.spring.init.domain.model;

public record AuthenticationToken(
    String authToken,
    String refreshToken
) {

  public static AuthenticationToken empty() {
    return new AuthenticationToken("", "");
  }
}
