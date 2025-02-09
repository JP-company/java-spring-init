package jjp.java.spring.init.domain.usecase;

import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.EXISTS_EMAIL;
import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.WRONG_ACCESS;
import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.WRONG_EMAIL_PATTERN;
import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.WRONG_NICKNAME_PATTERN;

import java.time.LocalDateTime;
import java.util.regex.Pattern;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.common.exception.AccountException;
import jjp.java.spring.init.domain.model.type.AccountStatus;

public record AccountRegister(
  String email,
  String nickname,
  LocalDateTime now
) {
  private static final Pattern NICKNAME_PATTERN = Pattern.compile(
    "^[가-힣a-zA-Z0-9]{2,6}$"
  );

  public AccountInsert toInsert() {
    return new AccountInsert(
      this.email,
      this.nickname,
      AccountStatus.ACTIVE,
      this.now
    );
  }

  public AccountRegister validate(
    boolean emailAuthCodeVerified,
    boolean existsByEmail
  ) {
    if (
      this.email == null ||
      !EmailAuthCodeCreator.EMAIL_PATTERN.matcher(this.email).matches()
    ) {
      throw new AccountException(WRONG_EMAIL_PATTERN);
    }
    if (
      this.nickname == null ||
      !NICKNAME_PATTERN.matcher(this.nickname).matches()
    ) {
      throw new AccountException(WRONG_NICKNAME_PATTERN);
    }
    if (!emailAuthCodeVerified) {
      throw new AccountException(WRONG_ACCESS);
    }
    if (existsByEmail) {
      throw new AccountException(EXISTS_EMAIL);
    }
    return this;
  }
}
