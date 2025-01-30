package jjp.java.spring.init.interfaces.interceptor;

import static jjp.java.spring.init.app.port.security.IAuthenticationTokenProvider.TOKEN_PREFIX;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

  public static String HEADER_STRING = "Authorization";

  private final IAccountDb accountDb;
  private final IAuthenticationTokenProvider authenticationTokenProvider;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (handler instanceof HandlerMethod handlerMethod) {
      boolean isPublicApi = handlerMethod.getMethod().isAnnotationPresent(PublicApi.class) ||
          handlerMethod.getBeanType().isAnnotationPresent(PublicApi.class);
      if (isPublicApi) {
        return true;
      }
    }

    String token = resolveToken(request);
    if (token == null) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or Missing Token");
      return false;
    }

    String accountId = authenticationTokenProvider.parseToken(token);
    Optional<Account> account = this.accountDb.findOneBy(Integer.parseInt(accountId));

    if (account.isEmpty()) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Account Not Found");
      return false;
    }

    request.setAttribute("account", account.get());
    return true;
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(HEADER_STRING);
    if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
