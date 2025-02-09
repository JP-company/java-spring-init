package jjp.java.spring.init.interfaces.interceptor;

import static jjp.java.spring.init.app.port.security.IAuthenticationTokenProvider.TOKEN_PREFIX;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;
import jjp.java.spring.init.app.port.db.IAccountDb;
import jjp.java.spring.init.app.port.security.IAuthenticationTokenProvider;
import jjp.java.spring.init.domain.model.Account;
import jjp.java.spring.init.interfaces.interceptor.annotation.PublicApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

  public static String AUTH_HEADER = "Authorization";
  public static String REFRESH_HEADER = "Refresh-Token";

  private final IAccountDb accountDb;
  private final IAuthenticationTokenProvider authenticationTokenProvider;

  @Override
  public boolean preHandle(
    HttpServletRequest request,
    HttpServletResponse response,
    Object handler
  ) throws Exception {
    System.out.println(request.getRequestURI());
    if (handler instanceof HandlerMethod handlerMethod) {
      boolean isPublicApi =
        handlerMethod.getMethod().isAnnotationPresent(PublicApi.class) ||
        handlerMethod.getBeanType().isAnnotationPresent(PublicApi.class);
      if (isPublicApi) {
        return true;
      }
    }

    String token = resolveToken(request);
    if (token == null || !token.startsWith(TOKEN_PREFIX)) {
      response.sendError(
        HttpServletResponse.SC_UNAUTHORIZED,
        "Invalid or Missing Token"
      );
      return false;
    }

    String accountId = authenticationTokenProvider.parseToken(
      token.substring(TOKEN_PREFIX.length())
    );
    Optional<Account> account =
      this.accountDb.findOneBy(Integer.parseInt(accountId));

    if (account.isEmpty()) {
      response.sendError(
        HttpServletResponse.SC_UNAUTHORIZED,
        "Account Not Found"
      );
      return false;
    }

    request.setAttribute("account", account.get());
    return true;
  }

  private String resolveToken(HttpServletRequest request) {
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if (AUTH_HEADER.equals(cookie.getName())) {
          return new String(Base64.getUrlDecoder().decode(cookie.getValue()));
        }
      }
    }
    return null;
  }
}
