package jjp.java.spring.init.domain.usecase;

import static jjp.java.spring.init.domain.common.error.AccountException.AccountErrorKey.EXISTS_PHONE_NUMBER;
import static jjp.java.spring.init.domain.common.error.AccountException.AccountErrorKey.INCORRECT_VERIFY_PASSWORD;
import static jjp.java.spring.init.domain.common.error.AccountException.AccountErrorKey.WRONG_NAME_PATTERN;
import static jjp.java.spring.init.domain.common.error.AccountException.AccountErrorKey.WRONG_PASSWORD_PATTERN;
import static jjp.java.spring.init.domain.common.error.AccountException.AccountErrorKey.WRONG_PHONE_NUMBER_PATTERN;

import java.time.LocalDateTime;
import java.util.regex.Pattern;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.common.error.AccountException;
import jjp.java.spring.init.domain.model.type.AccountStatus;

public record AccountRegister(
    String name,
    String phoneNumber,
    String hashedPassword,
    LocalDateTime now
) {

  private static final Pattern NAME_PATTERN = Pattern.compile("^[가-힣]{2,4}$");
  private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^01[0-9][0-9]{8}$");
  private static final Pattern PASSWORD_PATTERN = Pattern.compile(
      "^(?=.*[a-z])(?=.*\\d)(?=.*[!?@%^*+=])[a-zA-Z\\d!?@%^*+=]{8,20}$");

  public AccountInsert toInsert() {
    return new AccountInsert(
        this.name,
        this.phoneNumber,
        this.hashedPassword,
        AccountStatus.ACTIVE,
        this.now
    );
  }

  public AccountRegister validate(
      String password,
      String verifyPassword,
      boolean existsByPhoneNumber
  ) {
    if (!PASSWORD_PATTERN.matcher(password).matches()) {
      throw new AccountException(WRONG_PASSWORD_PATTERN);
    }
    if (!password.equals(verifyPassword)) {
      throw new AccountException(INCORRECT_VERIFY_PASSWORD);
    }
    if (!NAME_PATTERN.matcher(this.name).matches()) {
      throw new AccountException(WRONG_NAME_PATTERN);
    }
    if (!PHONE_NUMBER_PATTERN.matcher(this.phoneNumber).matches()) {
      throw new AccountException(WRONG_PHONE_NUMBER_PATTERN);
    }
    if (existsByPhoneNumber) {
      throw new AccountException(EXISTS_PHONE_NUMBER);
    }
    return this;
  }
}
