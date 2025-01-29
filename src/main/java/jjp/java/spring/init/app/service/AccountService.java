package jjp.java.spring.init.app.service;

import static jjp.java.spring.init.domain.common.error.AccountException.AccountErrorKey.NO_ACCOUNT;

import java.time.LocalDateTime;
import jjp.java.spring.init.app.port.db.AccountDb;
import jjp.java.spring.init.app.viewmodel.AccountGetOneViewModel;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.common.error.AccountException;
import jjp.java.spring.init.domain.model.Account;
import jjp.java.spring.init.domain.usecase.AccountRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountDb accountDb;

  public AccountGetOneViewModel getOne(int id) {
    Account account = this.accountDb.findOneBy(id)
        .orElseThrow(() -> new AccountException(NO_ACCOUNT));
    return new AccountGetOneViewModel(account);
  }

  public void register(String address, LocalDateTime now) {
    boolean existsByAddress = this.accountDb.existsBy(address);
    AccountInsert accountInsert = new AccountRegister(address, now)
        .validate(existsByAddress)
        .toInsert();
    this.accountDb.save(accountInsert);
  }
}
