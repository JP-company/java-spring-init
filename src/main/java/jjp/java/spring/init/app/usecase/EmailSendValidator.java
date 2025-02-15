package jjp.java.spring.init.app.usecase;

import java.util.regex.Pattern;
import org.springframework.lang.NonNull;

public class EmailSendValidator {

  private final String email;

  public static final Pattern EMAIL_PATTERN = Pattern.compile(
    "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
  );

  public EmailSendValidator(@NonNull final String email) {
    this.email = email;
  }

  public void run() {
    this.validate();
  }

  private void validate() {
    if (!EMAIL_PATTERN.matcher(this.email).matches()) {
      throw new IllegalArgumentException();
    }
  }
}
