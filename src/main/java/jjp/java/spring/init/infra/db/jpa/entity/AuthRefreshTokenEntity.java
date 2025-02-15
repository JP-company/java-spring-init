package jjp.java.spring.init.infra.db.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import jjp.java.spring.init.domain.command.RefreshTokenUpsert;
import jjp.java.spring.init.domain.model.auth.RefreshToken;
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
@Table(
  name = "auth_refresh_token",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uc_auth_refresh_token_refreshToken",
      columnNames = { "refreshToken" }
    ),
  }
)
public class AuthRefreshTokenEntity {

  @Id
  @Column(name = "account_id", nullable = false)
  private Integer accountId;

  // @OneToOne
  // @JoinColumn(name = "account_id", referencedColumnName = "id")
  // private AccountEntity account;

  @Column(name = "refresh_token", nullable = false, unique = true)
  private String refreshToken;

  @Column(name = "expiry_time", nullable = false)
  private LocalDateTime expiryTime;

  public static AuthRefreshTokenEntity of(RefreshTokenUpsert refreshInsert) {
    return new AuthRefreshTokenEntity(
      refreshInsert.accountId(),
      // null,
      refreshInsert.refreshToken(),
      refreshInsert.expiredAt()
    );
  }

  public RefreshToken toModel() {
    return new RefreshToken(this.accountId, this.refreshToken, this.expiryTime);
  }
}
