package jjp.java.spring.init.app.port.security;

public interface IAuthenticationTokenProvider {

  String TOKEN_PREFIX = "Bearer ";

  String generateToken(int id);

  String parseToken(String token);
}
