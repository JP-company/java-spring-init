package jjp.java.spring.init.interfaces.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record PostAccountAuthCodeBody(
    @Schema(description = "메일 주소", example = "wjsdj2008@gmail.com")
    String email
) {

}
