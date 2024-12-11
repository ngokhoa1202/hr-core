package org.hr.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hr.employee.dto.employee.EmployeeAssignmentStatisticsDto;
import org.hr.employee.utils.CommonRegex;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "employee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SqlResultSetMappings({
  @SqlResultSetMapping(
    name = "EmployeeAssignmentStatisticsDto",
    classes = {
      @ConstructorResult(
        targetClass = EmployeeAssignmentStatisticsDto.class,
        columns = {
          @ColumnResult(name = "id", type = UUID.class),
          @ColumnResult(name = "employeeId", type = String.class),
          @ColumnResult(name = "firstname", type = String.class),
          @ColumnResult(name = "lastname", type = String.class),
          @ColumnResult(name = "middlename", type = String.class),
          @ColumnResult(name = "dateOfBirth", type = LocalDateTime.class),
          @ColumnResult(name = "gender", type = GenderEnum.class),
          @ColumnResult(name = "salary", type = Integer.class),
          @ColumnResult(name = "numberOfAssignments", type = Long.class),
          @ColumnResult(name = "totalHours", type = Long.class),
          @ColumnResult(name = "averageHoursPerAssignment", type = Double.class)
        }
      )
    }
  )
})
@NamedEntityGraphs({
  @NamedEntityGraph(
    name = "Employee-department-graph",
    attributeNodes = @NamedAttributeNode("department")
  )
})
@NamedQueries({
  @NamedQuery(
    name = Employee.EMPLOYEES_BY_NAME_PREFIX_QUERY,
    query = "SELECT em FROM Employee em " +
      "WHERE LOWER(em.firstname) LIKE :name OR LOWER(em.lastname) LIKE :name OR LOWER(em.middlename) LIKE :name " +
      "ORDER BY em.firstname"
  )
})
@NamedNativeQueries({
  @NamedNativeQuery(
    name = Employee.EMPLOYEES_BY_EMPLOYEE_ID_PREFIX_QUERY,
    query = "SELECT * " +
      "FROM employee " +
      "INNER JOIN department " +
      "ON employee.deptid = department.department_id " +
      "WHERE employee.employee_id LIKE :employeeId",
    resultClass = Employee.class
  ),
  @NamedNativeQuery(
    name = Employee.EMPLOYEES_ASSIGNMENT_STATISTICS_BY_ID_QUERY,
    query = "SELECT e.id AS id, e.employee_id AS employeeId, e.first_name AS firstname, e.last_name AS lastname, e.middle_name AS middlename, " +
      "e.date_of_birth AS dateOfBirth, e.gender AS gender, e.salary AS salary, COUNT(assignment_id) AS numberOfAssignments, " +
      "SUM(a.number_of_hours) AS totalHours, AVG(a.number_of_hours) AS averageHoursPerAssignment " +
      "FROM Employee e " +
      "INNER JOIN Assignment a " +
      "ON e.employee_id = a.employee_assigned_id AND e.id = :id " +
      "GROUP BY e.id, e.employee_id, e.first_name, e.last_name, e.middle_name, e.date_of_birth, e.gender, e.salary",
    resultSetMapping = "EmployeeAssignmentStatisticsDto"
  )
})
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "employee_id", unique = true)
  private String employeeId;

  @Column(name = "first_name", nullable = false)
  @NotBlank
  @NotNull
  @Pattern(
    regexp = CommonRegex.NAME_REGEX
  )
  private String firstname;

  @Column(name = "last_name", nullable = false)
  @NotBlank
  @NotNull
  @Pattern(
    regexp = CommonRegex.NAME_REGEX
  )
  private String lastname;

  @Column(name = "middle_name", nullable = false)
  @NotBlank
  @NotNull
  @Pattern(
    regexp = CommonRegex.NAME_REGEX
  )
  private String middlename;

  @Column(name = "date_of_birth", nullable = false)
  private LocalDateTime dateOfBirth;

  @Column(name = "gender")
  @Enumerated(EnumType.STRING)
  private GenderEnum gender;

  @Column(name = "salary")
  @PositiveOrZero
  private Integer salary;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "deptid", referencedColumnName = "department_id")
  private Department department;

  @OneToMany(mappedBy = "employeeAssigned", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @Builder.Default
  private Set<Assignment> assignments = new HashSet<Assignment>();

  public static final String EMPLOYEES_BY_NAME_PREFIX_QUERY = "Employee.findByFirstnamePrefixOrLastnamePrefixOrMiddlenamePrefix";
  public static final String EMPLOYEES_BY_EMPLOYEE_ID_PREFIX_QUERY = "Employee.findByEmployeeIdPrefix";
  public static final String EMPLOYEES_ASSIGNMENT_STATISTICS_BY_ID_QUERY = "Employee.findAssignmentStatisticsById";
}
