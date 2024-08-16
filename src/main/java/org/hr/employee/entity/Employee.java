package org.hr.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hr.employee.dto.EmployeeDTO;
import org.hr.employee.utils.CommonRegex;

import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "employee")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NamedQueries({
  @NamedQuery(
    name = "findEmployeesByFirstname",
    query = "FROM Employee em WHERE em.firstname = :firstname" // more standard => remove SELECT still works
  ),
  @NamedQuery(
    name = "deleteEmployeeById",
    query = "DELETE Employee em WHERE em.id = :id"
  )
})

public class Employee {
  @Id
  @Column(name = "employee_id")
  private String id;

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
  private Date dateOfBirth;

  /* TODO: enum type */
  @Column(name = "gender")
  private String gender;

  @Column(name = "salary")
  @PositiveOrZero
  private Integer salary;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "deptid", referencedColumnName = "department_id")
  private Department department;

  @OneToMany(mappedBy = "employeeAssigned", fetch = FetchType.LAZY)
  private Set<Assignment> assignmentSet;

  public Employee setId(String id) {
    this.id = id;
    return this;
  }

  public Employee setFirstname(String firstname) {
    this.firstname = firstname;
    return this;
  }

  public Employee setLastname(String lastname) {
    this.lastname = lastname;
    return this;
  }

  public Employee setMiddlename(String middlename) {
    this.middlename = middlename;
    return this;
  }

  public Employee setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  public Employee setGender(String gender) {
    this.gender = gender;
    return this;
  }

  public Employee setSalary(Integer salary) {
    this.salary = salary;
    return this;
  }

  public Employee setDepartment(Department department) {
    this.department = department;
    return this;
  }

  public EmployeeDTO toEmployeeDTO() {

    return new EmployeeDTO(
      this.id, this.firstname, this.lastname, this.middlename, this.dateOfBirth, this.gender,
      this.salary, this.department.getId());
  }
}
