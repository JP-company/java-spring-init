package jjp.java.spring.init.app.viewmodel;

import java.time.LocalDateTime;
import jjp.java.spring.init.domain.model.Account;
import jjp.java.spring.init.domain.model.type.AccountStatus;

public record AccountGetOneViewModel(
    Account account
) {

  public int getAccountId() {
    return this.account.id();
  }

  public String getName() {
    return this.account.name();
  }

  public String getPhoneNumber() {
    return this.account.phoneNumber();
  }

  public AccountStatus getAccountStatus() {
    return this.account.status();
  }

  public LocalDateTime getCreatedAt() {
    return this.account.createdAt();
  }
}
