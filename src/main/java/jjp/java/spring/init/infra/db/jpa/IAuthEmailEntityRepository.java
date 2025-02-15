package jjp.java.spring.init.infra.db.jpa;

import java.time.LocalDateTime;
import java.util.Optional;
import jjp.java.spring.init.infra.db.jpa.entity.AuthEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IAuthEmailEntityRepository
  extends JpaRepository<AuthEmailEntity, Integer> {
  Optional<AuthEmailEntity> findOneByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByEmailAndAuthCode(String email, String authCode);

  @Modifying
  @Query(
    "UPDATE AuthEmailEntity SET accountId = :accountId WHERE email = :email"
  )
  int updateAccountIdByEmail(String email, int accountId);

  @Modifying
  @Query(
    "UPDATE AuthEmailEntity SET authCode = :authCode, expiryTime = :expiryTime WHERE email = :email"
  )
  int updateAuthCodeByEmail(
    String authCode,
    LocalDateTime expiryTime,
    String email
  );
}
