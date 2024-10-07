package org.hr.security.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hr.security.dto.role.RolePlainDto;

import java.util.UUID;

public record UserResponseDto(
  UUID id,
  String username,
  String password,
  String email,
  @JsonProperty(value = "role") RolePlainDto rolePlainDto
  ) {

}
