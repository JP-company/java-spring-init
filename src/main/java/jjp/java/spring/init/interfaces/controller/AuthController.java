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
import jjp.java.spring.init.app.service.AuthService;
import jjp.java.spring.init.domain.model.account.Account;
import jjp.java.spring.init.domain.model.auth.AuthenticationToken;
import jjp.java.spring.init.interfaces.dto.request.GetAuthEmailBody;
import jjp.java.spring.init.interfaces.dto.request.PostAuthEmailCodeBody;
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
@RequestMapping("/view/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PublicApi
  @PostMapping("/email/code")
  @Operation(summary = "이메일 인증코드 전송")
  public String postAuthEmailCode(
    @Valid @RequestBody PostAuthEmailCodeBody body,
    @RequestServerTime LocalDateTime now,
    Model model
  ) {
    this.authService.sendEmailAuthCode(body.email(), now);

    model.addAttribute("email", body.email());
    return "fragments/authCodeForm";
  }

  @PublicApi
  @PostMapping("/email")
  @Operation(summary = "이메일 인증코드 확인")
  public String getAuthEmail(
    @Valid @RequestBody GetAuthEmailBody body,
    @RequestServerTime LocalDateTime now,
    Model model,
    HttpServletResponse response
  ) {
    AuthenticationToken token =
      this.authService.authByEmail(body.email(), body.authCode(), now);

    if (token.isEmpty()) {
      model.addAttribute("email", body.email());
      model.addAttribute("authCode", body.authCode());
      return "fragments/joinForm";
    }

    this.addCookie(response, AUTH_HEADER, TOKEN_PREFIX + token.authToken());
    response.setHeader(REFRESH_HEADER, token.refreshToken());
    return "redirect:/home";
  }

  @PostMapping("/logout")
  @Operation(summary = "로그아웃")
  public String postAuthLogout(@RequestAccount Account account) {
    this.authService.logout(account.id());
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
