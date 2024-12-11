package org.hr.employee.dto.project.assignment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.hr.employee.dto.employee.EmployeePlainDto;
import org.hr.employee.dto.project.ProjectPlainDto;
import org.hr.employee.entity.Assignment;

import java.io.Serializable;

public record AssignmentPayloadDto(
  @NotNull @JsonProperty(value = "number_of_hours") Integer numberOfHours,
  @NotNull @JsonProperty(value = "employee") EmployeePlainDto employeePlainDto,
  @NotNull @JsonProperty(value = "project") ProjectPlainDto projectPlainDTO
) implements Serializable {

}
