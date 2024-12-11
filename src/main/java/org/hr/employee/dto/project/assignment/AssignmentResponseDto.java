package org.hr.employee.dto.project.assignment;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hr.employee.dto.employee.EmployeePlainDto;
import org.hr.employee.dto.employee.EmployeeResponseDto;
import org.hr.employee.dto.project.ProjectPlainDto;

public record AssignmentResponseDto(
  Long id,
  Integer numberOfHours,
  @JsonProperty(value = "employee") EmployeePlainDto employeePlainDto,
  @JsonProperty(value = "project")  ProjectPlainDto projectPlainDto
) {

}
