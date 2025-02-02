package jjp.java.spring.init.infra.db.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import jjp.java.spring.init.domain.model.EmailLimit;
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
@Table(name = "email_limit")
public class EmailLimitEntity {

  @Id
  @Column(name = "email", nullable = false, length = 50)
  private String email;

  @Column(name = "count")
  private Short count;

  @Column(name = "date", nullable = false)
  private LocalDate date;

  public EmailLimit toModel() {
    return new EmailLimit(this.email, this.count, this.date);
  }
}