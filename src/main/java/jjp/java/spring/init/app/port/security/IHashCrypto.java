package jjp.java.spring.init.app.port.security;

public interface IHashCrypto {

  String hash(String text);

  boolean matches(String text, String hashedText);
}
