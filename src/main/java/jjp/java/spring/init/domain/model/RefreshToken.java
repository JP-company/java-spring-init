package jjp.java.spring.init.domain.model;

import java.time.LocalDateTime;

public record RefreshToken(
    int accountId,
    String refreshToken,
    LocalDateTime expiryTime
) {

}
