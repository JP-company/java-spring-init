package jjp.java.spring.init.infra.db.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import jjp.java.spring.init.domain.command.EmailAuthCodeUpsert;
import jjp.java.spring.init.domain.model.EmailAuthCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "email_auth_code")
public class EmailAuthCodeEntity {

  @Id
  @Column(name = "email", nullable = false, length = 50)
  private String email;

  @Column(name = "auth_code", nullable = false, length = 10)
  private String authCode;

  @Column(name = "expiry_time", nullable = false)
  private LocalDateTime expiryTime;


  public static EmailAuthCodeEntity of(EmailAuthCodeUpsert emailAuthCodeUpsert) {
    return new EmailAuthCodeEntity(
        emailAuthCodeUpsert.email(),
        emailAuthCodeUpsert.authCode(),
        emailAuthCodeUpsert.expiryTime()
    );
  }

  public EmailAuthCode toModel() {
    return new EmailAuthCode(this.email, this.authCode, this.expiryTime);
  }
}