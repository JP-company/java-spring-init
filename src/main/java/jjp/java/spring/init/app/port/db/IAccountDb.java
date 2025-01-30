package jjp.java.spring.init.app.port.db;

import java.util.Optional;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.model.Account;
import jjp.java.spring.init.domain.model.AccountLogin;

public interface IAccountDb {

  void save(AccountInsert accountInsert);

  Optional<Account> findOneBy(int id);

  Optional<AccountLogin> findOneLoginBy(String phoneNumber);

  boolean existsBy(String phoneNumber);
}
