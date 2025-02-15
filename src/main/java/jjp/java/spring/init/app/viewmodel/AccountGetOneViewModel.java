package jjp.java.spring.init.app.viewmodel;

import java.time.LocalDateTime;
import jjp.java.spring.init.domain.model.account.Account;
import jjp.java.spring.init.domain.model.auth.AccountStatus;

public record AccountGetOneViewModel(Account account) {
  public int getAccountId() {
    return this.account.id();
  }

  public String getName() {
    return this.account.nickname();
  }

  public AccountStatus getAccountStatus() {
    return this.account.status();
  }

  public LocalDateTime getCreatedAt() {
    return this.account.createdAt();
  }
}
