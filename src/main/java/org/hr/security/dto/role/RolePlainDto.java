package org.hr.security.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hr.security.entity.Role;

public record RolePlainDto(
  @NotNull Integer id,
  @NotBlank String name
) {

  public Role toRole() {
    return Role.builder()
      .id(this.id)
      .name(this.name)
      .build();
  }
}
