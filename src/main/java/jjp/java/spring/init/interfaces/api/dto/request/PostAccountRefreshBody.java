package jjp.java.spring.init.interfaces.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PostAccountRefreshBody(
    @NotBlank
    @Schema(example = "sadf123-asdfsad213-sadf411")
    String refreshToken
) {

}
