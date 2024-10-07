package org.hr.security.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hr.security.dto.user.UserPlainDto;
import org.hr.security.dto.user.UserResponseDto;

import java.util.UUID;

@Entity
@Table(name = "`user`")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
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


  public UserPlainDto toUserDTO() {
    return new UserPlainDto(
      this.id,
      this.username,
      this.email,
      this.password
    );
  }

  public UserResponseDto toUserResponseDTO() {
    return new UserResponseDto(
      this.id,
      this.username,
      this.email,
      this.password,
      this.role.getName()
    );
  }
}
