package org.hr.security.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hr.security.dto.role.RolePlainDto;
import org.hr.security.entity.User;

import java.io.Serializable;


public record UserPayloadDto(
  @NotBlank String username,
  @NotBlank String password,
  @NotNull @Email String email,
  @NotNull @JsonProperty(value = "role") RolePlainDto rolePlainDto
) implements Serializable {

  public User toUser() {
    return User.builder()
      .username(this.username)
      .password(this.password)
      .email(this.email)
      .build();
  }
}
