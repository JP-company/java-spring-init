package jjp.java.spring.init.interfaces.controller.api;

import static jjp.java.spring.init.app.port.security.IAuthenticationTokenProvider.TOKEN_PREFIX;
import static jjp.java.spring.init.interfaces.interceptor.JwtAuthInterceptor.AUTH_HEADER;
import static jjp.java.spring.init.interfaces.interceptor.JwtAuthInterceptor.REFRESH_HEADER;

import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDateTime;
import jjp.java.spring.init.app.service.AuthService;
import jjp.java.spring.init.domain.model.auth.AuthenticationToken;
import jjp.java.spring.init.interfaces.dto.request.PostAccountRefreshBody;
import jjp.java.spring.init.interfaces.dto.response.EmptyResponse;
import jjp.java.spring.init.interfaces.interceptor.annotation.PublicApi;
import jjp.java.spring.init.interfaces.interceptor.annotation.RequestServerTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthApi {

  private final AuthService authService;

  @PublicApi
  @PostMapping("/refresh")
  @Operation(summary = "리프레시 토큰으로 인증 토큰 발급")
  public ResponseEntity<EmptyResponse> postAccountRefresh(
    @RequestBody PostAccountRefreshBody body,
    @RequestServerTime LocalDateTime now
  ) {
    AuthenticationToken token =
      this.authService.refresh(body.refreshToken(), now);
    return ResponseEntity.ok()
      .header(AUTH_HEADER, TOKEN_PREFIX + token.authToken())
      .header(REFRESH_HEADER, token.refreshToken())
      .body(EmptyResponse.SUCCESS);
  }
}
