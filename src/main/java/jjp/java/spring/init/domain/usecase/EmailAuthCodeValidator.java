package jjp.java.spring.init.domain.usecase;

import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.EXPIRED_EMAIL_AUTH_CODE;
import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.INCORRECT_EMAIL_AUTH_CODE;

import java.time.LocalDateTime;
import jjp.java.spring.init.domain.common.exception.AccountException;
import jjp.java.spring.init.domain.model.EmailAuthCode;

public record EmailAuthCodeValidator(
    EmailAuthCode emailAuthCode
) {

  public void validate(String authCode, LocalDateTime now) {
    if (emailAuthCode.expired(now)) {
      throw new AccountException(EXPIRED_EMAIL_AUTH_CODE);
    }
    if (!emailAuthCode.auth(authCode)) {
      throw new AccountException(INCORRECT_EMAIL_AUTH_CODE);
    }
  }
}
