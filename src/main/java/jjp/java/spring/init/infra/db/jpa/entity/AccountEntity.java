package jjp.java.spring.init.infra.db.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.model.Account;
import jjp.java.spring.init.domain.model.AccountLogin;
import jjp.java.spring.init.domain.model.type.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account", indexes = {
    @Index(name = "idx_account_name", columnList = "name"),
    @Index(name = "idx_account_status", columnList = "status"),
    @Index(name = "idx_account_created_at", columnList = "created_at"),
}, uniqueConstraints = {
    @UniqueConstraint(name = "uc_account_phone_number", columnNames = {"phone_number"})
})
public class AccountEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "name", nullable = false, length = 10)
  private String name;

  @Column(name = "phone_number", nullable = false, length = 20)
  private String phoneNumber;

  @Column(name = "hashed_password", nullable = false)
  private String hashedPassword;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 10)
  private AccountStatus status;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public static AccountEntity of(AccountInsert accountInsert) {
    return new AccountEntity(
        null,
        accountInsert.name(),
        accountInsert.phoneNumber(),
        accountInsert.hashedPassword(),
        accountInsert.status(),
        accountInsert.now()
    );
  }

  public Account toAccountModel() {
    return new Account(this.id, this.name, this.phoneNumber, this.status, this.createdAt);
  }

  public AccountLogin toAccountLoginModel() {
    return new AccountLogin(this.id, this.hashedPassword);
  }
}
