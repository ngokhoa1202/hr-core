package org.hr.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hr.employee.dto.AssignmentDTO;

@Entity
@Table(name = "assignment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@NamedQueries({
  @NamedQuery(
    name = "deleteAssignmentById",
    query = "DELETE Assignment a WHERE a.id = :id"
  )
})
public class Assignment {

  @Id
  @Column(name = "assignment_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @NotNull
  @PositiveOrZero
  private int numberOfHours;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(
    name = "employee_assigned_id",
    referencedColumnName = "employee_id"
  )
  private Employee employeeAssigned;

  @NotNull
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(
    name = "project_belonging_id",
    referencedColumnName = "project_id"
  )
  private Project projectBelonging;

  public Assignment setId(Long id) {
    this.id = id;
    return this;
  }

  public Assignment setNumberOfHours(int numberOfHours) {
    this.numberOfHours = numberOfHours;
    return this;
  }

  public Assignment setEmployeeAssigned(Employee employeeAssigned) {
    this.employeeAssigned = employeeAssigned;
    return this;
  }

  public Assignment setProjectBelonging(Project projectBelonging) {
    this.projectBelonging = projectBelonging;
    return this;
  }

  public AssignmentDTO toAssignmentDTO() {
    return new AssignmentDTO(this.id, this.numberOfHours, this.employeeAssigned.getId(), this.projectBelonging.getId());
  }
}
