package org.hr.security.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hr.security.dto.role.RolePlainDto;
import org.hr.security.dto.role.RoleResponseDto;

import java.util.Set;

@Entity
@Table(name = "`role`")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@NamedQueries({
  @NamedQuery(
    name = "findRoleByName",
    query = "SELECT r FROM Role r WHERE r.name = :name"
  )
})
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  @NotNull
  @NotBlank
  private String name;

  @OneToMany(
    mappedBy = "role",
    fetch = FetchType.LAZY,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  private Set<User> users;

}
