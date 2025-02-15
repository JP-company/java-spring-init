package jjp.java.spring.init.infra.db.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import jjp.java.spring.init.domain.command.EmailAuthCodeUpsert;
import jjp.java.spring.init.domain.model.auth.email.AuthEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
  name = "auth_email",
  uniqueConstraints = {
    @UniqueConstraint(name = "uc_auth_email_email", columnNames = { "email" }),
  }
)
public class AuthEmailEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "account_id")
  private Integer accountId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "account_id",
    referencedColumnName = "id",
    insertable = false,
    updatable = false
  )
  private AccountEntity account;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "auth_code", nullable = false)
  private String authCode;

  @Column(name = "expiry_time", nullable = false)
  private LocalDateTime expiryTime;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public static AuthEmailEntity of(EmailAuthCodeUpsert command) {
    return AuthEmailEntity.builder()
      .email(command.email())
      .authCode(command.authCode())
      .expiryTime(command.expiryTime())
      .build();
  }

  public AuthEmail toModel() {
    return new AuthEmail(
      this.accountId,
      this.email,
      this.authCode,
      this.expiryTime
    );
  }
}
