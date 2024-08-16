package org.hr.security.dto;

import org.hr.security.entity.Role;

import java.io.Serializable;

public record RoleCreationDTO(String name) implements Serializable {

  public Role toRole() {
    return Role.builder().name(this.name).build();
  }
}
