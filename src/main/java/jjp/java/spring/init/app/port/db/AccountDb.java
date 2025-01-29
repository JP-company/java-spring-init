package jjp.java.spring.init.app.port.db;

import java.util.Optional;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.model.Account;

public interface AccountDb {

  void save(AccountInsert accountInsert);

  Optional<Account> findOneBy(int id);

  boolean existsBy(String address);
}
