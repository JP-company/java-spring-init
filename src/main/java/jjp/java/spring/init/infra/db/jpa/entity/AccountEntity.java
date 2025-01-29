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
import java.time.LocalDateTime;
import jjp.java.spring.init.domain.command.AccountInsert;
import jjp.java.spring.init.domain.model.Account;
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
    @Index(name = "idx_account_status", columnList = "status"),
    @Index(name = "idx_account", columnList = "created_at")
})
public class AccountEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "address", length = 50, unique = true, nullable = false)
  private String address;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 20, nullable = false)
  private AccountStatus status;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public static AccountEntity of(AccountInsert accountInsert) {
    return new AccountEntity(
        null,
        accountInsert.address(),
        accountInsert.status(),
        accountInsert.now()
    );
  }

  public Account toModel() {
    return new Account(this.id, this.address, this.status, this.createdAt);
  }
}
