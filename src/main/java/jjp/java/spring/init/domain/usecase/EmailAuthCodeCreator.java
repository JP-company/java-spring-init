package jjp.java.spring.init.domain.usecase;

import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.EMAIL_LIMIT;

import java.time.LocalDateTime;
import jjp.java.spring.init.domain.command.EmailAuthCodeUpsert;
import jjp.java.spring.init.domain.common.exception.AccountException;
import jjp.java.spring.init.domain.model.EmailLimit;

public record EmailAuthCodeCreator(
  String email,
  String authCode,
  LocalDateTime expiryTime
) {
  private static final short SEND_LIMIT = 10;

  public EmailAuthCodeCreator validate(
    EmailLimit emailLimit,
    LocalDateTime now
  ) {
    if (
      now.toLocalDate().isEqual(emailLimit.date()) &&
      emailLimit.count() >= SEND_LIMIT
    ) {
      throw new AccountException(EMAIL_LIMIT);
    }
    return this;
  }

  public EmailAuthCodeUpsert toUpsert() {
    return new EmailAuthCodeUpsert(this.email, this.authCode, this.expiryTime);
  }
}
