package jjp.java.spring.init.domain.command;

import java.time.LocalDateTime;

public record RefreshTokenUpsert(
  int accountId,
  String refreshToken,
  LocalDateTime expiredAt
) {}
