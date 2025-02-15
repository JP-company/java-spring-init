package jjp.java.spring.init.infra.db;

import java.util.Optional;
import jjp.java.spring.init.app.port.db.IAuthDb;
import jjp.java.spring.init.domain.command.EmailAuthCodeUpsert;
import jjp.java.spring.init.domain.command.RefreshTokenUpsert;
import jjp.java.spring.init.domain.exception.AccountException;
import jjp.java.spring.init.domain.exception.AccountException.AccountErrorKey;
import jjp.java.spring.init.domain.model.auth.RefreshToken;
import jjp.java.spring.init.domain.model.auth.email.AuthEmail;
import jjp.java.spring.init.infra.db.jpa.IAuthEmailEntityRepository;
import jjp.java.spring.init.infra.db.jpa.IAuthRefreshEntityRepository;
import jjp.java.spring.init.infra.db.jpa.entity.AuthEmailEntity;
import jjp.java.spring.init.infra.db.jpa.entity.AuthRefreshTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthDb implements IAuthDb {

  private final IAuthEmailEntityRepository authEmailEntityRepository;
  private final IAuthRefreshEntityRepository loginRefreshEntityRepository;

  @Override
  public void upsertAuthCode(EmailAuthCodeUpsert emailAuthCodeUpsert) {
    Optional<AuthEmailEntity> entity =
      this.authEmailEntityRepository.findOneByEmail(
          emailAuthCodeUpsert.email()
        );
    if (entity.isPresent()) {
      this.authEmailEntityRepository.updateAuthCodeByEmail(
          emailAuthCodeUpsert.authCode(),
          emailAuthCodeUpsert.expiryTime(),
          emailAuthCodeUpsert.email()
        );
      return;
    }
    AuthEmailEntity authEmailEntity = AuthEmailEntity.of(emailAuthCodeUpsert);
    this.authEmailEntityRepository.save(authEmailEntity);
  }

  @Override
  public Optional<AuthEmail> findOneAuthEmailBy(String email) {
    return this.authEmailEntityRepository.findOneByEmail(email).map(
        AuthEmailEntity::toModel
      );
  }

  @Override
  public Optional<RefreshToken> findOneRefreshTokenBy(String refreshToken) {
    return this.loginRefreshEntityRepository.findOneByRefreshToken(
        refreshToken
      ).map(AuthRefreshTokenEntity::toModel);
  }

  @Override
  public void upsertRefreshToken(RefreshTokenUpsert loginRefreshTokenUpsert) {
    AuthRefreshTokenEntity refreshTokenEntity = AuthRefreshTokenEntity.of(
      loginRefreshTokenUpsert
    );
    this.loginRefreshEntityRepository.save(refreshTokenEntity);
  }

  @Override
  public void deleteRefreshToken(int accountId) {
    this.loginRefreshEntityRepository.deleteById(accountId);
  }

  @Override
  public void updateAuthEmail(String email, int accountId) {
    int affectedRows =
      this.authEmailEntityRepository.updateAccountIdByEmail(email, accountId);
    if (affectedRows == 0) {
      throw new AccountException(AccountErrorKey.WRONG_ACCESS);
    }
  }
}
