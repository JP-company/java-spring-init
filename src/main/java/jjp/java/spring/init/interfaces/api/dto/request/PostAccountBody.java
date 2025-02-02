package jjp.java.spring.init.interfaces.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PostAccountBody(
    @NotBlank
    @Schema(description = "이메일 주소", example = "wjsdj2008@gmail.com")
    String email,

    @NotBlank
    @Schema(description = "이메일 인증 코드", example = "123456")
    String authCode,

    @NotBlank
    @Schema(description = "닉네임", example = "전정표")
    String nickname

) {

}
