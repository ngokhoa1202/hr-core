package org.hr.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hr.employee.dto.ProjectDTO;
import org.hr.employee.utils.CommonRegex;

import java.util.Set;

@Entity
@Table(name = "project")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedQueries({
  @NamedQuery(
    name = "deleteProjectById",
    query = "DELETE Project p WHERE p.id = :id"
  )
})
public class Project {

  @Id
  @Column(name = "project_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @NotNull
  @NotBlank
  @Pattern(
    regexp = CommonRegex.NAME_REGEX
  )
  private String area;

  @Column(name = "project_name")
  @Pattern(
    regexp = CommonRegex.NAME_REGEX
  )
  private String name;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(
    name = "managed_department_id",
    referencedColumnName = "department_id"
  )
  private Department managedDepartment;

  @OneToMany(mappedBy = "projectBelonging", fetch = FetchType.LAZY)
  private Set<Assignment> assignmentSet;

  public Project setArea(String area) {
    this.area = area;
    return this;
  }

  public Project setId(Long id) {
    this.id = id;
    return this;
  }

  public Project setName(String name) {
    this.name = name;
    return this;
  }

  public Project setManagedDepartment(Department department) {
    this.managedDepartment = department;
    return this;
  }

  public ProjectDTO toProjectDTO() {
    return new ProjectDTO(this.id, this.name, this.area, this.managedDepartment.getId());
  }
}
