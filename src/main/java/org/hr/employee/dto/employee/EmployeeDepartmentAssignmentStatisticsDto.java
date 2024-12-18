package org.hr.employee.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hr.employee.dto.department.DepartmentPlainDto;
import org.hr.employee.entity.GenderEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public record EmployeeDepartmentAssignmentStatisticsDto(
  UUID id,
  String employeeId,
  String firstname,
  String lastname,
  String middlename,
  LocalDateTime dateOfBirth,
  GenderEnum gender,
  Integer salary,
  @JsonProperty(value = "department") DepartmentPlainDto departmentPlainDto,
  Double hoursSpentPerAssignment,
  Long totalHours,
  Long numberOfAssignments
) {

}
