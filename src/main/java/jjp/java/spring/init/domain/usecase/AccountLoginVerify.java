package jjp.java.spring.init.domain.usecase;

import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.INCORRECT_PASSWORD;

import jjp.java.spring.init.domain.common.exception.AccountException;

public record AccountLoginVerify(
    int accountId
) {

  public AccountLoginVerify validate(boolean passwordMatches) {
    if (!passwordMatches) {
      throw new AccountException(INCORRECT_PASSWORD);
    }
    return this;
  }
}
