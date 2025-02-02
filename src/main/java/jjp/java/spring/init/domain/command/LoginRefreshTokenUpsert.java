package jjp.java.spring.init.domain.command;

import java.time.LocalDateTime;

public record LoginRefreshTokenUpsert(
    int accountId,
    String refreshToken,
    LocalDateTime expiryTime
) {

}
