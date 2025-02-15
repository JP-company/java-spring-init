package jjp.java.spring.init.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PostAuthEmailCodeBody(
  @Schema(description = "메일 주소", example = "wjsdj2008@gmail.com")
  @Email
  @NotBlank
  String email
) {}
