package org.hr.security.dto.user;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record UserLoginDto(
  @NotBlank String username,
  @NotBlank String password
) implements Serializable {

}
