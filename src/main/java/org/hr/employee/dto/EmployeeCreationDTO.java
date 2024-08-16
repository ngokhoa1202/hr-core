package org.hr.employee.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hr.employee.entity.Employee;

import java.io.Serializable;
import java.sql.Date;

public record EmployeeCreationDTO(
  @JsonProperty(value = "employee_id") String employeeId,
  String firstname,
  String lastname,
  String middlename,
  @JsonProperty(value = "date_of_birth") Date dateOfBirth,
  String gender,
  Integer salary,
  @JsonProperty(value = "department_id") Long departmentId
) implements Serializable {

  public Employee toEmployee() {
    return Employee.builder()
      .id(this.employeeId)
      .firstname(this.firstname)
      .lastname(this.lastname)
      .middlename(this.middlename)
      .gender(this.gender)
      .salary(this.salary)
      .dateOfBirth(this.dateOfBirth)
      .build();
  }
}
