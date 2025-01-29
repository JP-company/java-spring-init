package jjp.java.spring.init.domain.model;

import java.time.LocalDateTime;
import jjp.java.spring.init.domain.model.type.AccountStatus;

public record Account(
    int id,
    String address,
    AccountStatus status,
    LocalDateTime createdAt
) {

}
