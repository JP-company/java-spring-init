package jjp.java.spring.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class InitApplication {

  public static void main(String[] args) {
    SpringApplication.run(InitApplication.class, args);
  }
}
