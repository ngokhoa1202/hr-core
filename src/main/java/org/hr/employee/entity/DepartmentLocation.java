package org.hr.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hr.employee.dto.department.location.DepartmentLocationResponseDTO;

@Entity
@Table(name = "department_location")
@Getter
@Setter
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

  @ManyToOne(
    fetch = FetchType.EAGER,
    cascade = {CascadeType.MERGE, CascadeType.PERSIST}
  )
  @JoinColumn(name = "deptid", referencedColumnName = "department_id")
  private Department department;


}
