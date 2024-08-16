package org.hr.security.entity;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hr.security.dto.UserDTO;

import java.util.UUID;

@Entity
@Table(name = "`user`")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@NamedQueries({
  @NamedQuery(
    name = "findUserByUsername",
    query = "SELECT u FROM User u WHERE u.username = :username"
  )
})
public class User {

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "username")
  private String username;

  @NotNull
  @NotBlank
  @Column(name = "password")
  private String password;

  @NotNull
  @NotBlank
  @Email
  @Column(name = "email")
  private String email;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(
    name = "role_id",
    referencedColumnName = "id",
    nullable = false
  )
  private Role role;

  public User setUsername(String username) {
    this.username = username;
    return this;
  }

  public User setPassword(String password) {
    this.password = password;
    return this;
  }

  public User setEmail(@Email String email) {
    this.email = email;
    return this;
  }

  public User setRole(Role role) {
    this.role = role;
    return this;
  }

  public UserDTO toUserDTO() {
    return UserDTO.builder()
      .id(this.id)
      .username(this.username)
      .password(this.password)
      .email(this.email)
      .roleName(this.role.getName())
      .build();
  }
}
