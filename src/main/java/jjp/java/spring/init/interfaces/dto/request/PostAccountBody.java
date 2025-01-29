package jjp.java.spring.init.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PostAccountBody(
    @NotBlank
    @Schema(description = "계정 주소", example = "wjsdj2008@gmail.com")
    String address
) {

}
