package jjp.java.spring.init.domain.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AccountException extends CustomException {

  public AccountException(AccountErrorKey accountErrorKey) {
    super(accountErrorKey);
  }

  @Getter
  @RequiredArgsConstructor
  public enum AccountErrorKey implements ErrorKey {
    NO_ACCOUNT("존재하지 않는 계정입니다"),
    EXISTS_ACCOUNT("이미 존재하는 이메일 입니다"),
    NOT_EMAIL_PATTERN("이메일 형식이 아닙니다");

    private final String koMessage;
  }
}
