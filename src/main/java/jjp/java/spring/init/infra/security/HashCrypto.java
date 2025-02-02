package jjp.java.spring.init.infra.security;

import jjp.java.spring.init.app.port.security.IHashCrypto;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class HashCrypto implements IHashCrypto {

  public String hash(String text) {
    return BCrypt.hashpw(text, BCrypt.gensalt());
  }

  public boolean matches(String text, String hashedText) {
    return BCrypt.checkpw(text, hashedText);
  }
}
