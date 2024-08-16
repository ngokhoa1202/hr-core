package org.hr.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hr.security.entity.User;

import java.io.Serializable;


public record UserCreationDTO(
  String username,
  String password,
  String email,
  @JsonProperty(value = "role_name") String roleName
) implements Serializable {

  public User toUser() {
    return User.builder()
      .username(this.username)
      .password(this.password)
      .email(this.email)
      .build();
  }
}
