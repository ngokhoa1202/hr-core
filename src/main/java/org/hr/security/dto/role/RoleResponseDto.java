package org.hr.security.dto.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hr.security.dto.user.UserPlainDto;

import java.io.Serializable;
import java.util.List;

public record RoleResponseDto(
  Integer id,
  String name
) implements Serializable {
}
