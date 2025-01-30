package jjp.java.spring.init.infra.sms;

import jjp.java.spring.init.app.port.sms.ISmsService;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;

@Service
public class SmsService implements ISmsService {

  private final DefaultMessageService messageService;

  public SmsService() {
    this.messageService = NurigoApp.INSTANCE.initialize(
        "NCSAER3IXISUP94G",
        "OSZ0FYLA4HPLGMBHAFAF80G3PY3FRFH8",
        "https://api.coolsms.co.kr"
    );
  }

  @Override
  public void send(String phoneNumber, String message) {
    Message smsMessage = new Message();
    smsMessage.setFrom("01087144246");
    smsMessage.setTo(phoneNumber);
    smsMessage.setText(message);
    this.messageService.sendOne(new SingleMessageSendingRequest(smsMessage));
  }
}
