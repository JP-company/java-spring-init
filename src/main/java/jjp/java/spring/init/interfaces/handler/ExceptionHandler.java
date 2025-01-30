package jjp.java.spring.init.interfaces.handler;

import jjp.java.spring.init.domain.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> exceptionHandler(CustomException e) {
    HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus().name());
    return ResponseEntity.status(httpStatus.value())
        .body(new ErrorResponse(httpStatus.value(), e.getKoMessage()));
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> exceptionHandler() {
    return ResponseEntity.badRequest()
        .body(new ErrorResponse(500, "eh1"));
  }
}
