package org.hr.security.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hr.security.dto.RoleDTO;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "`role`")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString(exclude = "userSet")
@EqualsAndHashCode(exclude = "userSet")
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

  @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
  private Set<User> userSet;

  public Role setId(Integer id) {
    this.id = id;
    return this;
  }

  public Role setName(String name) {
    this.name = name;
    return this;
  }

  public Role setUserSet(HashSet<User> userSet) {
    this.userSet = userSet;
    return this;
  }

  public RoleDTO toRoleDTO() {
    return RoleDTO.builder().id(this.id).name(this.name).build();
  }
}
