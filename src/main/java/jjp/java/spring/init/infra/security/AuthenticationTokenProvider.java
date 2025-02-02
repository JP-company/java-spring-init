package jjp.java.spring.init.infra.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import jjp.java.spring.init.app.port.security.IAuthenticationTokenProvider;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationTokenProvider implements IAuthenticationTokenProvider {

  private static final String SECRET_KEY = "your-256-bit-secret-your-256-bit-secret";
  private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간
  private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

  @Override
  public String generateToken(int id) {
    return Jwts.builder()
        .subject(String.valueOf(id))
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(key)
        .compact();
  }

  @Override
  public String parseToken(String token) {
    Claims body = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
    return body.getSubject();
  }
}
