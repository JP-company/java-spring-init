package jjp.java.spring.init.infra.db;

import java.util.Optional;
import jjp.java.spring.init.app.port.db.IAccountDb;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.model.account.Account;
import jjp.java.spring.init.infra.db.jpa.IAccountEntityRepository;
import jjp.java.spring.init.infra.db.jpa.entity.AccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountDb implements IAccountDb {

  private final IAccountEntityRepository accountEntityRepository;

  @Override
  public int insert(AccountInsert accountInsert) {
    AccountEntity accountEntity = AccountEntity.of(accountInsert);
    return this.accountEntityRepository.save(accountEntity).getId();
  }

  @Override
  public Optional<Account> findOneBy(int id) {
    return this.accountEntityRepository.findOneById(id).map(
        AccountEntity::toAccountModel
      );
  }
}
