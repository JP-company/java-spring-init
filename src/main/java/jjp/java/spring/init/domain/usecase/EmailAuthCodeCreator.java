package jjp.java.spring.init.domain.usecase;

import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.EMAIL_LIMIT;
import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.WRONG_EMAIL_PATTERN;

import java.time.LocalDateTime;
import java.util.regex.Pattern;
import jjp.java.spring.init.domain.command.EmailAuthCodeUpsert;
import jjp.java.spring.init.domain.common.exception.AccountException;
import jjp.java.spring.init.domain.model.EmailLimit;

public record EmailAuthCodeCreator(
  String email,
  String authCode,
  LocalDateTime expiryTime
) {
  public static final Pattern EMAIL_PATTERN = Pattern.compile(
    "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
  );
  private static final short SEND_LIMIT = 10;

  public EmailAuthCodeCreator validate(
    EmailLimit emailLimit,
    LocalDateTime now
  ) {
    if (this.email == null || !EMAIL_PATTERN.matcher(this.email).matches()) {
      throw new AccountException(WRONG_EMAIL_PATTERN);
    }
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
