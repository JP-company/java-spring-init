package jjp.java.spring.init.infra.db.jpa;

import jjp.java.spring.init.infra.db.jpa.entity.EmailAuthCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmailAuthCodeEntityRepository extends
    JpaRepository<EmailAuthCodeEntity, String> {

  boolean existsByEmailAndAuthCode(String email, String authCode);
}