package jjp.java.spring.init.domain.common.exception;

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
    EXISTS_EMAIL("이미 존재하는 이메일 입니다"),
    WRONG_NAME_PATTERN("잘못된 이름 입니다"),
    INCORRECT_VERIFY_PASSWORD("비밀번호 확인이 불일치 합니다"),
    INCORRECT_PASSWORD("비밀번호가 일치하지 않습니다"),
    WRONG_PASSWORD_PATTERN("비밀번호 형식은 8~20자의 영대소문자, 숫자, 특수문자 !?@%^*+= 가 꼭 포함되어야 합니다"),
    WRONG_REFRESH_TOKEN("다시 로그인 해주세요"),
    WRONG_ACCESS("잘못된 접근입니다"),
    EXPIRED_REFRESH_TOKEN("로그인 시간이 만료되었습니다. 다시 로그인 해주세요"),
    EXPIRED_EMAIL_AUTH_CODE("인증 시간이 만료되었습니다"),
    INCORRECT_EMAIL_AUTH_CODE("인증 번호가 일치하지 않습니다");

    private final String koMessage;
  }
}
