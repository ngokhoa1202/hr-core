package org.hr.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hr.employee.dto.project.assignment.AssignmentPlainDto;
import org.hr.employee.dto.project.assignment.AssignmentResponseDto;

@Entity
@Table(name = "assignment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Assignment {

  @Id
  @Column(name = "assignment_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @NotNull
  @PositiveOrZero
  private Integer numberOfHours;

  @ManyToOne(
    fetch = FetchType.EAGER,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE }
  )
  @JoinColumn(
    name = "employee_assigned_id",
    referencedColumnName = "employee_id"
  )
  private Employee employeeAssigned;

  @NotNull
  @ManyToOne(
    fetch = FetchType.EAGER,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  @JoinColumn(
    name = "project_belonging_id",
    referencedColumnName = "project_id"
  )
  private Project projectBelonging;

}
