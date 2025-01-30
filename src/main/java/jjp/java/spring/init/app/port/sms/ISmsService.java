package jjp.java.spring.init.app.port.sms;

public interface ISmsService {

  void send(String phoneNumber, String message);
}
