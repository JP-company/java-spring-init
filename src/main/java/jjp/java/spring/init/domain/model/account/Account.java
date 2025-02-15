package jjp.java.spring.init.domain.model.account;

import java.time.LocalDateTime;
import jjp.java.spring.init.domain.model.auth.AccountStatus;

public record Account(
  int id,
  String nickname,
  AccountStatus status,
  LocalDateTime createdAt
) {}
