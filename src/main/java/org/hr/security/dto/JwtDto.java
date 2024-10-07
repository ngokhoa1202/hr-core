package org.hr.security.dto;

import java.io.Serializable;

public record JwtDto(
  String token
) implements Serializable {

}
