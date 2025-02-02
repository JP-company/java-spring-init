package jjp.java.spring.init.interfaces.api;

import static jjp.java.spring.init.app.port.security.IAuthenticationTokenProvider.TOKEN_PREFIX;
import static jjp.java.spring.init.interfaces.interceptor.JwtAuthInterceptor.AUTH_HEADER;
import static jjp.java.spring.init.interfaces.interceptor.JwtAuthInterceptor.REFRESH_HEADER;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.LocalDateTime;
import jjp.java.spring.init.app.service.AccountService;
import jjp.java.spring.init.domain.model.Account;
import jjp.java.spring.init.domain.model.AuthenticationToken;
import jjp.java.spring.init.interfaces.api.dto.request.GetAccountStartParam;
import jjp.java.spring.init.interfaces.api.dto.request.PostAccountAuthCodeBody;
import jjp.java.spring.init.interfaces.api.dto.request.PostAccountBody;
import jjp.java.spring.init.interfaces.api.dto.request.PostAccountRefreshBody;
import jjp.java.spring.init.interfaces.api.dto.response.EmptyResponse;
import jjp.java.spring.init.interfaces.interceptor.annotation.PublicApi;
import jjp.java.spring.init.interfaces.interceptor.annotation.RequestAccount;
import jjp.java.spring.init.interfaces.interceptor.annotation.RequestServerTime;
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

  @PublicApi
  @PostMapping("/auth-code")
  @Operation(summary = "이메일 인증코드 전송 요청")
  public EmptyResponse postAccountAuthCode(
      @RequestBody PostAccountAuthCodeBody body,
      @RequestServerTime LocalDateTime now
  ) {
    this.accountService.sendAuthCode(body.email(), now);
    return EmptyResponse.SUCCESS;
  }

  @PublicApi
  @GetMapping("/start")
  @Operation(summary = "시작하기")
  public ResponseEntity<EmptyResponse> getAccountStart(
      @Parameter GetAccountStartParam param,
      @RequestServerTime LocalDateTime now
  ) {
    AuthenticationToken token = this.accountService.start(
        param.email(),
        param.authCode(),
        now
    );
    return ResponseEntity.ok()
        .header(AUTH_HEADER, TOKEN_PREFIX + token.authToken())
        .header(REFRESH_HEADER, token.refreshToken())
        .body(EmptyResponse.SUCCESS);
  }

  @PublicApi
  @PostMapping("")
  @Operation(summary = "계정 등록")
  public ResponseEntity<EmptyResponse> postAccount(
      @RequestBody PostAccountBody body,
      @RequestServerTime LocalDateTime now
  ) {
    AuthenticationToken token = this.accountService.register(
        body.email(),
        body.authCode(),
        body.nickname(),
        now
    );
    return ResponseEntity.ok()
        .header(AUTH_HEADER, TOKEN_PREFIX + token.authToken())
        .header(REFRESH_HEADER, token.refreshToken())
        .body(EmptyResponse.SUCCESS);
  }

  @PublicApi
  @PostMapping("/refresh")
  @Operation(summary = "리프레시 토큰으로 인증 토큰 발급")
  public ResponseEntity<EmptyResponse> postAccountRefresh(
      @RequestBody PostAccountRefreshBody body,
      @RequestServerTime LocalDateTime now
  ) {
    AuthenticationToken token = this.accountService.refresh(
        body.refreshToken(),
        now
    );
    return ResponseEntity.ok()
        .header(AUTH_HEADER, TOKEN_PREFIX + token.authToken())
        .header(REFRESH_HEADER, token.refreshToken())
        .body(EmptyResponse.SUCCESS);
  }

  @PostMapping("/logout")
  @Operation(summary = "로그아웃")
  public EmptyResponse postAccountLogout(
      @RequestAccount Account account
  ) {
    this.accountService.logout(account.id());
    return EmptyResponse.SUCCESS;
  }
}
