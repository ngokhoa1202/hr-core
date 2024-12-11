package org.hr.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Table(name = "assignments")
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

  @NotNull
  @NotBlank
  private String title;

  private String description;

  @ManyToOne(
    fetch = FetchType.EAGER,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE }
  )
  @JoinColumn(
    name = "employee__id",
    referencedColumnName = "employee_id"
  )
  private Employee employeeAssigned;

  @NotNull
  @ManyToOne(
    fetch = FetchType.EAGER,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  @JoinColumn(
    name = "project_id",
    referencedColumnName = "project_id"
  )
  private Project projectBelonging;

}
