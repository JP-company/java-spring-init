package jjp.java.spring.init.infra.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jjp.java.spring.init.app.port.email.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class EmailService implements IEmailService {

  private final JavaMailSender javaMailSender;
  private final String EMAIL_FROM = "smartwire";

  @Override
  public void send(String emailTo, String title, String content) {
    try {
      MimeMessage emailForm = javaMailSender.createMimeMessage();
      emailForm.addRecipients(MimeMessage.RecipientType.TO, emailTo);
      emailForm.setSubject(title);
      emailForm.setFrom(EMAIL_FROM);
      emailForm.setText(content, "utf-8", "html");
      javaMailSender.send(emailForm);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}
