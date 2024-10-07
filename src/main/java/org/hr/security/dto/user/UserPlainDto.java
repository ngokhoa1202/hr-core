package org.hr.security.dto.user;

import java.io.Serializable;
import java.util.UUID;


public record UserPlainDto(
  UUID id,
  String username,
  String email,
  String password
) implements Serializable {

}
