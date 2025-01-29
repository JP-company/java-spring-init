package jjp.java.spring.init.domain.common.error;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  private final ErrorKey key;
  private final Status status;

  public CustomException(ErrorKey key) {
    this.key = key;
    this.status = Status.BAD_REQUEST;
  }

  public String getKoMessage() {
    return this.key.getKoMessage();
  }

  public enum Status {
    INTERNAL_SERVER_ERROR,
    BAD_REQUEST,
    NOT_FOUND
  }
}

