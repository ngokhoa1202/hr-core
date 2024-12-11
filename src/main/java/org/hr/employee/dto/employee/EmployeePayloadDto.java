package org.hr.employee.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hr.employee.dto.department.DepartmentPlainDto;
import org.hr.employee.entity.Employee;
import org.hr.employee.entity.GenderEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public record EmployeePayloadDto(
  @NotBlank String employeeId,
  @NotBlank String firstname,
  @NotBlank String lastname,
  @NotBlank String middlename,
  @NotNull LocalDateTime dateOfBirth,
  @NotBlank String gender,
  @NotNull @PositiveOrZero Integer salary,
  @JsonProperty(value = "department") @NotNull DepartmentPlainDto departmentPlainDto
) implements Serializable {


}
