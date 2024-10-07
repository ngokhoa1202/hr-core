package org.hr.employee.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hr.employee.entity.GenderEnum;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record EmployeeAssignmentStatisticsDto(
  UUID id,
  String employeeId,
  String firstname,
  String lastname,
  String middlename,
  LocalDateTime dateOfBirth,
  GenderEnum gender,
  Integer salary,
  Long numberOfAssignments,
  Long totalHours,
  Double averageHoursPerAssignment
) implements Serializable {
}
