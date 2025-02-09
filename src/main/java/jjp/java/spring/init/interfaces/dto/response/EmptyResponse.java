package jjp.java.spring.init.interfaces.dto.response;

public record EmptyResponse() {
  public static EmptyResponse SUCCESS = new EmptyResponse();
}
