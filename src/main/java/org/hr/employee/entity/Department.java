package org.hr.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hr.employee.dto.DepartmentDTO;
import org.hr.employee.utils.CommonRegex;

import java.sql.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@NamedQueries({
  @NamedQuery(
    name = "deleteDepartmentById",
    query = "DELETE Department dept WHERE dept.id = :id"
  ),
  @NamedQuery(
    name = "findDepartmentByName",
    query = "SELECT d FROM Department d WHERE d.name = :name"
  )
})
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "department_id")
  private Long id;

  @Column(name = "department_name", unique = true, nullable = false)
  @NotNull
  @NotBlank
  @Pattern(
    regexp = CommonRegex.NAME_REGEX
  )
  private String name;

  @Column(name = "start_date", nullable = false)
  @NotNull
  private Date startDate;

  /* Specify department property in class DepartmentLocation  */
  @OneToOne(mappedBy = "department", fetch = FetchType.EAGER)
  private DepartmentLocation location;

  @OneToMany(mappedBy = "managedDepartment", fetch = FetchType.LAZY)
  private Set<Project> projectSet;

  public Department setId(Long id) {
    this.id = id;
    return this;
  }

  public Department setName(String name) {
    this.name = name;
    return this;
  }

  public Department setStartDate(Date startDate) {
    this.startDate = startDate;
    return this;
  }

  public Department setLocation(DepartmentLocation location) {
    this.location = location;
    return this;
  }

  public Department setProjectSet(Set<Project> projectSet) {
    this.projectSet = projectSet;
    return this;
  }

  public DepartmentDTO toDepartmentDTO() {
    return new DepartmentDTO(this.id, this.name, this.startDate);
  }

}
