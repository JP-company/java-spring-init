package jjp.java.spring.init.infra.db.jpa;

import java.util.Optional;
import jjp.java.spring.init.infra.db.jpa.entity.LoginRefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILoginRefreshEntityRepository extends
    JpaRepository<LoginRefreshTokenEntity, Integer> {

  Optional<LoginRefreshTokenEntity> findOneByRefreshToken(String refreshToken);
}