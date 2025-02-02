package jjp.java.spring.init.infra.db.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import jjp.java.spring.init.domain.command.LoginRefreshTokenUpsert;
import jjp.java.spring.init.domain.model.RefreshToken;
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
@Table(name = "login_refresh_token")
public class LoginRefreshTokenEntity {

  @Id
  @Column(name = "account_id", nullable = false)
  private Integer accountId;

  @OneToOne
  @PrimaryKeyJoinColumn(name = "account_id", referencedColumnName = "id")
  private AccountEntity account;

  @Column(name = "refresh_token", nullable = false, unique = true)
  private String refreshToken;

  @Column(name = "expiry_time", nullable = false)
  private LocalDateTime expiryTime;

  public static LoginRefreshTokenEntity of(LoginRefreshTokenUpsert refreshInsert) {
    return new LoginRefreshTokenEntity(
        refreshInsert.accountId(),
        null,
        refreshInsert.refreshToken(),
        refreshInsert.expiryTime()
    );
  }

  public RefreshToken toModel() {
    return new RefreshToken(this.accountId, this.refreshToken, this.expiryTime);
  }
}
