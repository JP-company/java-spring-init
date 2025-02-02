package jjp.java.spring.init.infra.db.jpa;

import java.time.LocalDate;
import jjp.java.spring.init.infra.db.jpa.entity.EmailLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface IEmailLimitEntityRepository extends JpaRepository<EmailLimitEntity, String> {

  @Modifying
  @Transactional
  @Query("UPDATE EmailLimitEntity e SET e.count = e.count + 1 WHERE e.email = :email")
  int incrementCountByEmail(@Param("email") String email);

  @Transactional
  @Modifying
  @Query("update EmailLimitEntity e set e.count = 1, e.date = :date where e.email = :email")
  int updateCountAndDateByEmail(String email, LocalDate date);

}