package jjp.java.spring.init.interfaces.controller;

import static jjp.java.spring.init.app.port.security.IAuthenticationTokenProvider.TOKEN_PREFIX;
import static jjp.java.spring.init.interfaces.interceptor.JwtAuthInterceptor.AUTH_HEADER;
import static jjp.java.spring.init.interfaces.interceptor.JwtAuthInterceptor.REFRESH_HEADER;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Base64;
import jjp.java.spring.init.app.service.AccountService;
import jjp.java.spring.init.domain.model.Account;
import jjp.java.spring.init.domain.model.AuthenticationToken;
import jjp.java.spring.init.interfaces.dto.request.GetAccountStartBody;
import jjp.java.spring.init.interfaces.dto.request.PostAccountAuthCodeBody;
import jjp.java.spring.init.interfaces.dto.request.PostAccountBody;
import jjp.java.spring.init.interfaces.interceptor.annotation.PublicApi;
import jjp.java.spring.init.interfaces.interceptor.annotation.RequestAccount;
import jjp.java.spring.init.interfaces.interceptor.annotation.RequestServerTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/v1/account")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;
  private static final String HOME_PAGE_REDIRECT = "redirect:/home";

  @PublicApi
  @PostMapping("/auth-code")
  @Operation(summary = "이메일 인증코드 전송 요청")
  public String postAccountAuthCode(
    @Valid @RequestBody PostAccountAuthCodeBody body,
    @RequestServerTime LocalDateTime now,
    Model model
  ) {
    this.accountService.sendAuthCode(body.email(), now);

    model.addAttribute("email", body.email());
    return "fragments/authCodeForm";
  }

  @PublicApi
  @PostMapping("/start")
  @Operation(summary = "시작하기")
  public String getAccountStart(
    @Valid @RequestBody GetAccountStartBody body,
    @RequestServerTime LocalDateTime now,
    Model model,
    HttpServletResponse response
  ) {
    AuthenticationToken token =
      this.accountService.start(body.email(), body.authCode(), now);

    if (token.isEmpty()) {
      model.addAttribute("email", body.email());
      model.addAttribute("authCode", body.authCode());
      return "fragments/startForm";
    }

    addCookie(response, AUTH_HEADER, TOKEN_PREFIX + token.authToken());
    response.setHeader(REFRESH_HEADER, token.refreshToken());
    return HOME_PAGE_REDIRECT;
  }

  @PublicApi
  @PostMapping("")
  @Operation(summary = "계정 등록")
  public String postAccount(
    @RequestBody PostAccountBody body,
    @RequestServerTime LocalDateTime now,
    HttpServletResponse response
  ) {
    AuthenticationToken token =
      this.accountService.register(
          body.email(),
          body.authCode(),
          body.nickname(),
          now
        );

    addCookie(response, AUTH_HEADER, TOKEN_PREFIX + token.authToken());
    response.setHeader(REFRESH_HEADER, token.refreshToken());
    return HOME_PAGE_REDIRECT;
  }

  @PostMapping("/logout")
  @Operation(summary = "로그아웃")
  public String postAccountLogout(@RequestAccount Account account) {
    this.accountService.logout(account.id());
    return "redirect:/";
  }

  private void addCookie(
    HttpServletResponse response,
    String name,
    String value
  ) {
    String encodedValue = Base64.getUrlEncoder()
      .encodeToString(value.getBytes());
    Cookie cookie = new Cookie(name, encodedValue);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    // cookie.setSecure(true);
    cookie.setMaxAge(60 * 60);
    response.addCookie(cookie);
  }
}
