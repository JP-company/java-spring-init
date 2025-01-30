package jjp.java.spring.init.interfaces.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record GetAccountParam(
    @Schema(example = "1")
    int accountId
) {

}
