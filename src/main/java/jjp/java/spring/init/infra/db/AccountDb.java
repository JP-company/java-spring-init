package jjp.java.spring.init.infra.db;

import java.time.LocalDate;
import java.util.Optional;
import jjp.java.spring.init.app.port.db.IAccountDb;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.command.EmailAuthCodeUpsert;
import jjp.java.spring.init.domain.command.LoginRefreshTokenUpsert;
import jjp.java.spring.init.domain.model.Account;
import jjp.java.spring.init.domain.model.AccountLogin;
import jjp.java.spring.init.domain.model.EmailAuthCode;
import jjp.java.spring.init.domain.model.EmailLimit;
import jjp.java.spring.init.domain.model.RefreshToken;
import jjp.java.spring.init.infra.db.jpa.IAccountEntityRepository;
import jjp.java.spring.init.infra.db.jpa.IEmailAuthCodeEntityRepository;
import jjp.java.spring.init.infra.db.jpa.IEmailLimitEntityRepository;
import jjp.java.spring.init.infra.db.jpa.ILoginRefreshEntityRepository;
import jjp.java.spring.init.infra.db.jpa.entity.AccountEntity;
import jjp.java.spring.init.infra.db.jpa.entity.EmailAuthCodeEntity;
import jjp.java.spring.init.infra.db.jpa.entity.EmailLimitEntity;
import jjp.java.spring.init.infra.db.jpa.entity.LoginRefreshTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountDb implements IAccountDb {

  private final IEmailLimitEntityRepository emailLimitEntityRepository;
  private final IAccountEntityRepository accountEntityRepository;
  private final ILoginRefreshEntityRepository loginRefreshEntityRepository;
  private final IEmailAuthCodeEntityRepository emailAuthCodeEntityRepository;

  @Override
  public int insert(AccountInsert accountInsert) {
    AccountEntity accountEntity = AccountEntity.of(accountInsert);
    return this.accountEntityRepository.save(accountEntity).getId();
  }

  @Override
  public Optional<EmailAuthCode> findOneEmailAuthCodeBy(String email) {
    return this.emailAuthCodeEntityRepository.findById(email)
        .map(EmailAuthCodeEntity::toModel);
  }

  @Override
  public boolean existsByEmailAndAuthCode(String email, String authCode) {
    return this.emailAuthCodeEntityRepository.existsByEmailAndAuthCode(email, authCode);
  }

  @Override
  public Optional<EmailLimit> findOneEmailLimit(String email) {
    return this.emailLimitEntityRepository.findById(email)
        .map(EmailLimitEntity::toModel);
  }

  @Override
  public Optional<Account> findOneBy(int id) {
    return this.accountEntityRepository.findOneById(id)
        .map(AccountEntity::toAccountModel);
  }

  @Override
  public Optional<Account> findOneBy(String email) {
    return accountEntityRepository.findOneByEmail(email)
        .map(AccountEntity::toAccountModel);
  }

  @Override
  public void upsertAuthCode(EmailAuthCodeUpsert emailAuthCodeUpsert) {
    this.emailAuthCodeEntityRepository.save(EmailAuthCodeEntity.of(emailAuthCodeUpsert));
  }

  @Override
  public Optional<AccountLogin> findOneLoginBy(String email) {
    return this.accountEntityRepository.findOneByEmail(email)
        .map(AccountEntity::toAccountLoginModel);
  }

  @Override
  public boolean existsBy(String email) {
    return this.accountEntityRepository.existsByEmail(email);
  }

  @Override
  public void upsertRefreshToken(LoginRefreshTokenUpsert refreshTokenUpsert) {
    this.loginRefreshEntityRepository.save(LoginRefreshTokenEntity.of(refreshTokenUpsert));
  }

  @Override
  public Optional<RefreshToken> findOneRefreshTokenBy(String refreshToken) {
    return this.loginRefreshEntityRepository.findOneByRefreshToken(refreshToken)
        .map(LoginRefreshTokenEntity::toModel);
  }

  @Override
  public void deleteRefreshToken(int accountId) {
    this.loginRefreshEntityRepository.deleteById(accountId);
  }

  @Override
  public void incrementEmailLimit(String email) {
    int affected = this.emailLimitEntityRepository.incrementCountByEmail(email);
    if (affected != 1) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public EmailLimit saveEmailLimit(String email, int count, LocalDate now) {
    EmailLimitEntity entity = new EmailLimitEntity(email, (short) count, now);
    return this.emailLimitEntityRepository.save(entity).toModel();
  }
}
