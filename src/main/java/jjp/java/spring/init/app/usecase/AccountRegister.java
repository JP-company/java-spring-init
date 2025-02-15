package jjp.java.spring.init.app.usecase;

import static jjp.java.spring.init.domain.exception.AccountException.AccountErrorKey.WRONG_NICKNAME_PATTERN;

import java.time.LocalDateTime;
import java.util.regex.Pattern;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.exception.AccountException;
import jjp.java.spring.init.domain.model.auth.AccountStatus;
import jjp.java.spring.init.domain.model.auth.email.AuthEmail;
import org.springframework.lang.NonNull;

public record AccountRegister(
  @NonNull AuthEmail authEmail,
  @NonNull String nickname,
  @NonNull LocalDateTime now
) {
  private static final Pattern NICKNAME_PATTERN = Pattern.compile(
    "^[가-힣a-zA-Z0-9]{2,6}$"
  );

  public AccountInsert run() {
    this.validate();
    return new AccountInsert(
      this.authEmail.email(),
      this.nickname,
      AccountStatus.ACTIVE,
      this.now
    );
  }

  public void validate() {
    if (!NICKNAME_PATTERN.matcher(this.nickname).matches()) {
      throw new AccountException(WRONG_NICKNAME_PATTERN);
    }
  }
}
