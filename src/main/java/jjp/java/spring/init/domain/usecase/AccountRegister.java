package jjp.java.spring.init.domain.usecase;

import static jjp.java.spring.init.domain.common.error.AccountException.AccountErrorKey.EXISTS_ACCOUNT;
import static jjp.java.spring.init.domain.common.error.AccountException.AccountErrorKey.NOT_EMAIL_PATTERN;

import java.time.LocalDateTime;
import java.util.regex.Pattern;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.common.error.AccountException;
import jjp.java.spring.init.domain.model.type.AccountStatus;

public record AccountRegister(
    String address,
    LocalDateTime now
) {

  private static final Pattern EMAIL_PATTERN = Pattern.compile(
      "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

  public AccountInsert toInsert() {
    return new AccountInsert(this.address, AccountStatus.ACTIVE, this.now);
  }

  public AccountRegister validate(boolean existsByAddress) {
    if (!EMAIL_PATTERN.matcher(this.address).matches()) {
      throw new AccountException(NOT_EMAIL_PATTERN);
    }
    if (existsByAddress) {
      throw new AccountException(EXISTS_ACCOUNT);
    }
    return this;
  }
}
