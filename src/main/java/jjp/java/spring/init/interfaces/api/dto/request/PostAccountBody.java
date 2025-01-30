package jjp.java.spring.init.interfaces.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PostAccountBody(
    @NotBlank
    @Schema(description = "이름", example = "전정표")
    String name,

    @NotBlank
    @Schema(description = "핸드폰 번호", example = "01087144246")
    String phoneNumber,

    @NotBlank
    @Schema(description = "비밀번호", example = "QWERasdf1234!@")
    String password,

    @NotBlank
    @Schema(description = "비밀번호 확인", example = "QWERasdf1234!@")
    String verifyPassword
) {

}
