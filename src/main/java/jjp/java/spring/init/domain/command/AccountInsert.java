package jjp.java.spring.init.domain.command;

import java.time.LocalDateTime;
import jjp.java.spring.init.domain.model.type.AccountStatus;

public record AccountInsert(
    String address,
    AccountStatus status,
    LocalDateTime now
) {

}
