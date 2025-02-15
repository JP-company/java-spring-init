package jjp.java.spring.init.app.port.db;

import java.util.Optional;
import jjp.java.spring.init.domain.command.EmailAuthCodeUpsert;
import jjp.java.spring.init.domain.command.RefreshTokenUpsert;
import jjp.java.spring.init.domain.model.auth.RefreshToken;
import jjp.java.spring.init.domain.model.auth.email.AuthEmail;

public interface IAuthDb {
  void upsertAuthCode(EmailAuthCodeUpsert emailAuthCodeUpsert);

  Optional<AuthEmail> findOneAuthEmailBy(String email);

  void upsertRefreshToken(RefreshTokenUpsert loginRefreshTokenUpsert);

  Optional<RefreshToken> findOneRefreshTokenBy(String refreshToken);

  void deleteRefreshToken(int accountId);

  void updateAuthEmail(String email, int accountId);
}
