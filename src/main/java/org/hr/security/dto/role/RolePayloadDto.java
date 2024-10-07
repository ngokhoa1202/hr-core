package org.hr.security.dto.role;

import jakarta.validation.constraints.NotBlank;
import org.hr.security.entity.Role;

import java.io.Serializable;

public record RolePayloadDto(
  @NotBlank String name
) implements Serializable {

  public Role toRole() {
    return Role.builder().name(this.name).build();
  }
}
