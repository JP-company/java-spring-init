package jjp.java.spring.init.domain.model;

import java.time.LocalDateTime;
import jjp.java.spring.init.domain.model.type.AccountStatus;

public record Account(
    int id,
    String name,
    String phoneNumber,
    AccountStatus status,
    LocalDateTime createdAt
) {

}
