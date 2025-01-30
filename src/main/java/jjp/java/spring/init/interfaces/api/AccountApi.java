package jjp.java.spring.init.interfaces.api;

import static jjp.java.spring.init.app.port.security.IAuthenticationTokenProvider.TOKEN_PREFIX;
import static jjp.java.spring.init.interfaces.interceptor.JwtAuthInterceptor.HEADER_STRING;

import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDateTime;
import jjp.java.spring.init.app.service.AccountService;
import jjp.java.spring.init.app.viewmodel.AccountGetOneViewModel;
import jjp.java.spring.init.domain.model.Account;
import jjp.java.spring.init.interfaces.api.dto.request.PostAccountBody;
import jjp.java.spring.init.interfaces.api.dto.request.PostAccountLoginBody;
import jjp.java.spring.init.interfaces.api.dto.response.EmptyResponse;
import jjp.java.spring.init.interfaces.api.dto.response.GetAccountResponse;
import jjp.java.spring.init.interfaces.interceptor.annotation.PublicApi;
import jjp.java.spring.init.interfaces.interceptor.annotation.RequestAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountApi {

  private final AccountService accountService;

  @GetMapping("")
  @Operation(summary = "계정 조회")
  public GetAccountResponse getAccount(
      @RequestAccount Account account
  ) {
    AccountGetOneViewModel viewModel = new AccountGetOneViewModel(account);
    return new GetAccountResponse(viewModel);
  }

  @PostMapping("")
  @Operation(summary = "계정 생성")
  @PublicApi
  public EmptyResponse postAccount(
      @RequestBody PostAccountBody body
  ) {
    LocalDateTime now = LocalDateTime.now();
    this.accountService.register(
        body.name(),
        body.phoneNumber(),
        body.password(),
        body.verifyPassword(),
        now
    );
    return EmptyResponse.SUCCESS;
  }

  @PostMapping("/login")
  @Operation(summary = "로그인")
  @PublicApi
  public ResponseEntity<EmptyResponse> postAccountLogin(
      @RequestBody PostAccountLoginBody body
  ) {
    String jwtToken = this.accountService.login(
        body.phoneNumber(),
        body.password()
    );
    return ResponseEntity.ok()
        .header(HEADER_STRING, TOKEN_PREFIX + jwtToken)
        .body(EmptyResponse.SUCCESS);
  }
}
