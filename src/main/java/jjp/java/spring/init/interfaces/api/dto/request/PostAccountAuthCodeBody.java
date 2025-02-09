package jjp.java.spring.init.interfaces.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PostAccountAuthCodeBody(
  @Schema(description = "메일 주소", example = "wjsdj2008@gmail.com")
  @NotBlank
  String email
) {}
