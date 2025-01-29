package jjp.java.spring.init.infra.db.jpa;

import java.util.Optional;
import jjp.java.spring.init.infra.db.jpa.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccountRepository extends JpaRepository<AccountEntity, Integer> {

  Optional<AccountEntity> findOneById(int id);

  boolean existsByAddress(String address);
}