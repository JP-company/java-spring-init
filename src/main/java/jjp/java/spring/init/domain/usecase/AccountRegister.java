package jjp.java.spring.init.domain.usecase;

import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.EXISTS_EMAIL;
import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.WRONG_ACCESS;

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
      "^(?=.*[a-z])(?=.*\\d)(?=.*[!?@%^*+=])[a-zA-Z\\d!?@%^*+=]{8,20}$");

  public AccountInsert toInsert() {
    return new AccountInsert(
        this.email,
        this.nickname,
        AccountStatus.ACTIVE,
        this.now
    );
  }

  public AccountRegister validate(boolean emailAuthCodeVerified, boolean existsByEmail) {
    if (!emailAuthCodeVerified) {
      throw new AccountException(WRONG_ACCESS);
    }
    if (existsByEmail) {
      throw new AccountException(EXISTS_EMAIL);
    }
    return this;
  }
}
