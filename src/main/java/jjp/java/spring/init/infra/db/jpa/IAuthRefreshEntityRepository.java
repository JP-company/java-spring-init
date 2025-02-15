package jjp.java.spring.init.infra.db.jpa;

import java.util.Optional;
import jjp.java.spring.init.infra.db.jpa.entity.AuthRefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthRefreshEntityRepository
  extends JpaRepository<AuthRefreshTokenEntity, Integer> {
  Optional<AuthRefreshTokenEntity> findOneByRefreshToken(String refreshToken);
}
