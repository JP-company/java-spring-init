package jjp.java.spring.init.infra.db;

import java.util.Optional;
import jjp.java.spring.init.app.port.db.IAccountDb;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.model.Account;
import jjp.java.spring.init.domain.model.AccountLogin;
import jjp.java.spring.init.infra.db.jpa.IAccountRepository;
import jjp.java.spring.init.infra.db.jpa.entity.AccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountDb implements IAccountDb {

  private final IAccountRepository accountRepository;

  public void save(AccountInsert accountInsert) {
    AccountEntity accountEntity = AccountEntity.of(accountInsert);
    this.accountRepository.save(accountEntity);
  }

  public Optional<Account> findOneBy(int id) {
    return this.accountRepository.findOneById(id)
        .map(AccountEntity::toAccountModel);
  }

  @Override
  public Optional<AccountLogin> findOneLoginBy(String phoneNumber) {
    return this.accountRepository.findOneByPhoneNumber(phoneNumber)
        .map(AccountEntity::toAccountLoginModel);
  }

  public boolean existsBy(String phoneNumber) {
    return this.accountRepository.existsByPhoneNumber(phoneNumber);
  }
}
