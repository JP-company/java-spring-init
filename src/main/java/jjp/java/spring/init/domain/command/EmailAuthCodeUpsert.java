package jjp.java.spring.init.domain.command;

import java.time.LocalDateTime;

public record EmailAuthCodeUpsert(
    String email,
    String authCode,
    LocalDateTime expiryTime
) {

}
