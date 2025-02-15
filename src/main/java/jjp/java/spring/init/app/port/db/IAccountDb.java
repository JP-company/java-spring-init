package jjp.java.spring.init.app.port.db;

import java.util.Optional;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.model.account.Account;

public interface IAccountDb {
  int insert(AccountInsert accountInsert);

  Optional<Account> findOneBy(int id);
}
