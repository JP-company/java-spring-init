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

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountApi {

  private final AccountService accountService;

  @PublicApi
  @PostMapping("/auth-code")
  @Operation(summary = "이메일 인증코드 전송 요청")
  public String postAccountAuthCode(
    @RequestBody PostAccountAuthCodeBody body,
    @RequestServerTime LocalDateTime now
  ) {
    this.accountService.sendAuthCode(body.email(), now);

    return (
      "<div id='step'>" +
      "<form hx-get='/api/v1/account/start' hx-target='#step' hx-swap='outerHTML'>" +
      "<label><input type='email' name='email' value='" +
      body.email() +
      "' readonly></label>" +
      "<label><input type='text' name='authCode' placeholder='인증 코드 입력' required></label>" +
      "<button type='submit'>인증 완료</button>" +
      "</form></div>"
    );
  }

  @PublicApi
  @GetMapping("/start")
  @Operation(summary = "시작하기")
  public ResponseEntity<String> getAccountStart(
    @Parameter GetAccountStartParam param,
    @RequestServerTime LocalDateTime now
  ) {
    AuthenticationToken token =
      this.accountService.start(param.email(), param.authCode(), now);

    if (token.isEmpty()) {
      return ResponseEntity.ok(
        "<div id='step'>" +
        "<form hx-post='/api/v1/account' hx-target='#step' hx-swap='outerHTML' hx-ext='json-enc'>" +
        "<label><input type='text' name='email' value='" +
        param.email() +
        "' readonly></label>" +
        "<label><input type='text' name='authCode' value='" +
        param.authCode() +
        "' readonly></label>" +
        "<label><input type='text' name='nickname' placeholder='닉네임 입력' required></label>" +
        "<button type='submit'>회원가입</button>" +
        "</form></div>"
      );
    }

    return ResponseEntity.ok()
      .header(AUTH_HEADER, TOKEN_PREFIX + token.authToken())
      .header(REFRESH_HEADER, token.refreshToken())
      .body("");
  }

  @PublicApi
  @PostMapping("")
  @Operation(summary = "계정 등록")
  public ResponseEntity<EmptyResponse> postAccount(
    @RequestBody PostAccountBody body,
    @RequestServerTime LocalDateTime now
  ) {
    AuthenticationToken token =
      this.accountService.register(
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
    AuthenticationToken token =
      this.accountService.refresh(body.refreshToken(), now);
    return ResponseEntity.ok()
      .header(AUTH_HEADER, TOKEN_PREFIX + token.authToken())
      .header(REFRESH_HEADER, token.refreshToken())
      .body(EmptyResponse.SUCCESS);
  }

  @PostMapping("/logout")
  @Operation(summary = "로그아웃")
  public EmptyResponse postAccountLogout(@RequestAccount Account account) {
    this.accountService.logout(account.id());
    return EmptyResponse.SUCCESS;
  }
}
