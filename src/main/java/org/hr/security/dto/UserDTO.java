package org.hr.security.dto;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {
  private UUID id;
  private String username;
  private String email;
  private String password;
  private String roleName;
}
