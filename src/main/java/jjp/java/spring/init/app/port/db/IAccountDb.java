package jjp.java.spring.init.app.port.db;

import java.time.LocalDate;
import java.util.Optional;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.command.EmailAuthCodeUpsert;
import jjp.java.spring.init.domain.command.LoginRefreshTokenUpsert;
import jjp.java.spring.init.domain.model.Account;
import jjp.java.spring.init.domain.model.AccountLogin;
import jjp.java.spring.init.domain.model.EmailAuthCode;
import jjp.java.spring.init.domain.model.EmailLimit;
import jjp.java.spring.init.domain.model.RefreshToken;

public interface IAccountDb {

  int insert(AccountInsert accountInsert);

  Optional<EmailAuthCode> findOneEmailAuthCodeBy(String email);

  boolean existsByEmailAndAuthCode(String email, String authCode);

  Optional<EmailLimit> findOneEmailLimit(String email);

  Optional<Account> findOneBy(int id);

  Optional<Account> findOneBy(String email);

  void upsertAuthCode(EmailAuthCodeUpsert emailAuthCodeUpsert);

  Optional<AccountLogin> findOneLoginBy(String email);

  boolean existsBy(String email);

  void upsertRefreshToken(LoginRefreshTokenUpsert refreshInsert);

  Optional<RefreshToken> findOneRefreshTokenBy(String RefreshToken);

  void deleteRefreshToken(int accountId);

  void incrementEmailLimit(String email);

  EmailLimit saveEmailLimit(String email, int count, LocalDate now);
}
