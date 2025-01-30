package jjp.java.spring.init.domain.command;

import java.time.LocalDateTime;
import jjp.java.spring.init.domain.model.type.AccountStatus;

public record AccountInsert(
    String name,
    String phoneNumber,
    String hashedPassword,
    AccountStatus status,
    LocalDateTime now
) {

}
