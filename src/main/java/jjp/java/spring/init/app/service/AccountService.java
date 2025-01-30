package jjp.java.spring.init.app.service;

import static jjp.java.spring.init.domain.common.exception.AccountException.AccountErrorKey.NO_ACCOUNT;

import java.time.LocalDateTime;
import jjp.java.spring.init.app.port.db.IAccountDb;
import jjp.java.spring.init.app.port.security.IAuthenticationTokenProvider;
import jjp.java.spring.init.app.port.security.IHashCrypto;
import jjp.java.spring.init.app.port.sms.ISmsService;
import jjp.java.spring.init.app.viewmodel.AccountGetOneViewModel;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.common.exception.AccountException;
import jjp.java.spring.init.domain.model.Account;
import jjp.java.spring.init.domain.model.AccountLogin;
import jjp.java.spring.init.domain.usecase.AccountLoginVerify;
import jjp.java.spring.init.domain.usecase.AccountRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final IAccountDb accountDb;

  private final IHashCrypto hashCrypto;
  private final IAuthenticationTokenProvider authenticationTokenProvider;
  private final ISmsService smsService;

  public AccountGetOneViewModel getOne(int accountId) {
    Account account = this.accountDb.findOneBy(accountId)
        .orElseThrow(() -> new AccountException(NO_ACCOUNT));
    return new AccountGetOneViewModel(account);
  }

  public void register(
      String name,
      String phoneNumber,
      String password,
      String verifyPassword,
      LocalDateTime now
  ) {
    boolean existsByPhoneNumber = this.accountDb.existsBy(phoneNumber);
    String hashedPassword = this.hashCrypto.hash(password);

    AccountInsert accountInsert = new AccountRegister(name, phoneNumber, hashedPassword, now)
        .validate(password, verifyPassword, existsByPhoneNumber)
        .toInsert();
    this.accountDb.save(accountInsert);
  }

  public String login(String phoneNumber, String password) {
    AccountLogin accountLogin = this.accountDb.findOneLoginBy(phoneNumber)
        .orElseThrow(() -> new AccountException(NO_ACCOUNT));
    boolean passwordMatches = this.hashCrypto.matches(password, accountLogin.hashedPassword());

    int accountId = new AccountLoginVerify(accountLogin.id())
        .validate(passwordMatches)
        .accountId();
    return this.authenticationTokenProvider.generateToken(accountId);
  }
}
