package org.hr.employee.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hr.employee.dto.department.DepartmentPlainDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record EmployeeResponseDto(
  UUID id,
  String employeeId,
  String firstname,
  String lastname,
  String middlename,
  LocalDateTime dateOfBirth,
  String gender,
  Integer salary,
  @JsonProperty(value = "department") DepartmentPlainDto departmentPlainDto
  ) implements Serializable {

}
