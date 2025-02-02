package jjp.java.spring.init.interfaces.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jjp.java.spring.init.app.viewmodel.AccountStartStatus;

public record GetAccountStartResponse(
    @Schema(description = "회원 상태", example = "JOIN")
    AccountStartStatus status
) {

}
