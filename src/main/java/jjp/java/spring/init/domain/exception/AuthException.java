package jjp.java.spring.init.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AuthException extends CustomException {

  public AuthException(AuthErrorKey authErrorKey) {
    super(authErrorKey);
  }

  @Getter
  @RequiredArgsConstructor
  public enum AuthErrorKey implements ErrorKey {
    EMAIL_LIMIT("오늘 하루 이메일 전송량을 초과했습니다(하루 10회)");

    private final String koMessage;
  }
}
