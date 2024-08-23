package org.hr.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hr.employee.dto.DepartmentLocationDTO;

import java.io.Serializable;

@Entity
@Table(name = "department_location")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedQueries({
  @NamedQuery(
    name = "deleteLocationById",
    query = "DELETE DepartmentLocation location WHERE location.id = :id"
  ),
  @NamedQuery(
    name = "findDepartmentLocationByLocation",
    query = "SELECT l FROM DepartmentLocation l WHERE l.location = :location"
  ),
  @NamedQuery(
    name = "findDepartmentLocationByDepartment",
    query = "SELECT l FROM DepartmentLocation l WHERE l.department = :department"
  )
})
public class DepartmentLocation {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "location", unique = true, nullable = false)
  @NotNull
  @NotBlank
  private String location;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "deptid", referencedColumnName = "department_id")
  private Department department;

  public DepartmentLocation setId(Long id) {
    this.id = id;
    return this;
  }

  public DepartmentLocation setLocation(String location) {
    this.location = location;
    return this;
  }

  public DepartmentLocation setDepartment(Department department) {
    this.department = department;
    return this;
  }

  public DepartmentLocationDTO toDepartmentLocationDTO() {
    return new DepartmentLocationDTO(this.id, this.location, this.department.getId());
  }

}
