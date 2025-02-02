package jjp.java.spring.init.domain.model;

import java.time.LocalDate;

public record EmailLimit(
    String email,
    short count,
    LocalDate date
) {

  public static EmailLimit empty(String email, LocalDate now) {
    return new EmailLimit(email, (short) 0, now);
  }
}
