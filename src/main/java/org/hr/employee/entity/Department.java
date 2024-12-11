package org.hr.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hr.employee.dto.department.DepartmentEmployeeStatisticsDto;
import org.hr.employee.utils.CommonRegex;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@SqlResultSetMappings({
  @SqlResultSetMapping(
    name = "DepartmentEmployeeStatisticsDto",
    classes = {
      @ConstructorResult(
        targetClass = DepartmentEmployeeStatisticsDto.class,
        columns = {
          @ColumnResult(name = "id", type = Long.class),
          @ColumnResult(name = "name", type = String.class),
          @ColumnResult(name = "startDate", type = LocalDateTime.class),
          @ColumnResult(name = "numberOfEmployees", type = Long.class),
          @ColumnResult(name = "totalSalary", type = Long.class),
          @ColumnResult(name = "maximumSalary", type = Long.class),
          @ColumnResult(name = "minimumSalary", type = Long.class),
          @ColumnResult(name = "averageSalary", type = Double.class)
        }
      )
    }
  )
})

@NamedEntityGraphs({
  @NamedEntityGraph(
    name = Department.DEPARTMENT_EMPLOYEE_GRAPH,
    attributeNodes = {
      @NamedAttributeNode(value = "id"),
      @NamedAttributeNode(value = "name"),
      @NamedAttributeNode(value = "startDate"),
      @NamedAttributeNode(value = "employees")
    }
  ),
  @NamedEntityGraph(
    name = Department.DEPARTMENT_LOCATIONS_GRAPH,
    attributeNodes = {
      @NamedAttributeNode(value = "id"),
      @NamedAttributeNode(value = "name"),
      @NamedAttributeNode(value = "startDate"),
      @NamedAttributeNode(value = "locations")
    }
  )
})

@NamedNativeQueries({
  @NamedNativeQuery(
    name = Department.DEPARTMENT_EMPLOYEE_STATISTICS_BY_ID_QUERY,
    query =
      """
      SELECT dept.department_id AS id, dept.department_name AS name, dept.start_date AS startDate,
      COUNT(emp.employee_id) AS numberOfEmployees, SUM(emp.salary) AS totalSalary, MAX(emp.salary) AS maximumSalary,
        MIN(emp.salary) AS minimumSalary, AVG(emp.salary) AS averageSalary
      FROM Department dept
      INNER JOIN employee emp on dept.department_id = emp.deptid AND dept.department_id = :id
      GROUP BY dept.department_id, dept.department_name, dept.start_date
      """,
    resultSetMapping = "DepartmentEmployeeStatisticsDto"
  )
})
@NamedQueries({
  @NamedQuery(
    name = Department.DEPARTMENT_EMPLOYEES_WITH_LOWEST_HOURS_SPENT_PER_ASSIGNMENTS_IN_DESCENDING_ORDER_BY_ID,
    query = "SELECT dept.id AS departmentId, dept.name AS departmentName, dept.startDate AS departmentStartDate, " +
      "emp.id AS employeeUUID, emp.employeeId AS employeeId, emp.firstname AS firstname, emp.middlename AS middlename, " +
      "emp.lastname AS lastname, emp.dateOfBirth AS dateOfBirth, emp.salary AS salary, emp.gender AS gender, " +
      "AVG(ass.numberOfHours) AS hoursSpentPerAssignment, SUM(ass.numberOfHours) AS totalHours, " +
      "COUNT(ass.id) AS numberOfAssignments " +
      "FROM Department AS dept " +
      "INNER JOIN dept.projects AS proj " +
        "WITH proj.managedDepartment.id = :id " +
      "INNER JOIN dept.employees AS emp " +
      "INNER JOIN emp.assignments AS ass " +
      "GROUP BY dept.id, emp.id " +
      "ORDER BY hoursSpentPerAssignment desc " +
      "LIMIT :limit"
  ),
  @NamedQuery(
    name = Department.DEPARTMENT_EMPLOYEES_WITH_LOWEST_HOURS_SPENT_PER_ASSIGNMENTS_IN_ASCENDING_ORDER_BY_ID,
    query = "SELECT dept.id AS departmentId, dept.name AS departmentName, dept.startDate AS departmentStartDate, " +
      "emp.id AS employeeUUID, emp.employeeId AS employeeId, emp.firstname AS firstname, emp.middlename AS middlename, " +
      "emp.lastname AS lastname, emp.dateOfBirth AS dateOfBirth, emp.salary AS salary, emp.gender AS gender, " +
      "AVG(ass.numberOfHours) AS hoursSpentPerAssignment, SUM(ass.numberOfHours) AS totalHours, " +
      "COUNT(ass.id) AS numberOfAssignments " +
      "FROM Department AS dept " +
      "INNER JOIN dept.projects AS proj " +
      "WITH proj.managedDepartment.id = :id " +
      "INNER JOIN dept.employees AS emp " +
      "INNER JOIN emp.assignments AS ass " +
      "GROUP BY dept.id, emp.id " +
      "ORDER BY hoursSpentPerAssignment asc " +
      "LIMIT :limit"
  ),
  @NamedQuery(
    name = Department.DEPARTMENT_LOCATIONS_QUERY,
    query = "SELECT dept FROM Department dept"
  )
})

public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "department_id")
  @EqualsAndHashCode.Include
  private Long id;

  @Column(name = "department_name", unique = true, nullable = false)
  @NotNull
  @NotBlank
  @Pattern(
    regexp = CommonRegex.NAME_REGEX
  )
  @EqualsAndHashCode.Include
  private String name;

  @Column(name = "start_date", nullable = false)
  @NotNull
  @EqualsAndHashCode.Include
  private LocalDateTime startDate;

  @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
  @Builder.Default
  @EqualsAndHashCode.Exclude
  private Set<Employee> employees = new HashSet<>();

  @OneToMany(
    mappedBy = "department",
    cascade = {CascadeType.MERGE, CascadeType.PERSIST},
    fetch = FetchType.LAZY
  )
  @Builder.Default
  @EqualsAndHashCode.Exclude
  private Set<DepartmentLocation> locations = new HashSet<>();

  @OneToMany(mappedBy = "managedDepartment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @Builder.Default
  @EqualsAndHashCode.Exclude
  private Set<Project> projects = new HashSet<>();

  public static final String DEPARTMENT_EMPLOYEE_GRAPH = "Department.Department-employee-graph";
  public static final String DEPARTMENT_EMPLOYEE_STATISTICS_BY_ID_QUERY = "Department.findDepartmentStatisticsById";
  public static final String DEPARTMENT_EMPLOYEES_WITH_LOWEST_HOURS_SPENT_PER_ASSIGNMENTS_IN_DESCENDING_ORDER_BY_ID =
    "Department.findEmployeeWithLowestHoursSpentPerAssignmentsInDescendingOrder";
  public static final String DEPARTMENT_EMPLOYEES_WITH_LOWEST_HOURS_SPENT_PER_ASSIGNMENTS_IN_ASCENDING_ORDER_BY_ID =
    "Department.findEmployeeWithLowestHoursSpentPerAssignmentsInAscendingOrder";
  public static final String DEPARTMENT_LOCATIONS_GRAPH = "Department.Department-locations-graph";
  public static final String DEPARTMENT_LOCATIONS_QUERY = "Department.findDepartmentsWithLocations";
}
