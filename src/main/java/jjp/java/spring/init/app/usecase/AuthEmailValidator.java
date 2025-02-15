package jjp.java.spring.init.app.usecase;

import static jjp.java.spring.init.domain.exception.AccountException.AccountErrorKey.EXPIRED_EMAIL_AUTH_CODE;
import static jjp.java.spring.init.domain.exception.AccountException.AccountErrorKey.INCORRECT_EMAIL_AUTH_CODE;

import java.time.LocalDateTime;
import jjp.java.spring.init.domain.exception.AccountException;
import jjp.java.spring.init.domain.model.auth.email.AuthEmail;

public record AuthEmailValidator(
  AuthEmail authEmail,
  String inputAuthCode,
  LocalDateTime now
) {
  public void run() {
    this.validateExpired();
    this.validateAuthCode();
  }

  private void validateExpired() {
    if (authEmail.expired(now)) {
      throw new AccountException(EXPIRED_EMAIL_AUTH_CODE);
    }
  }

  private void validateAuthCode() {
    if (!authEmail.equals(inputAuthCode)) {
      throw new AccountException(INCORRECT_EMAIL_AUTH_CODE);
    }
  }
}
