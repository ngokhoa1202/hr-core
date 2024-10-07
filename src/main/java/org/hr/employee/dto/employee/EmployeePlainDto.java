package org.hr.employee.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hr.employee.dto.department.DepartmentPlainDto;
import org.jboss.resteasy.reactive.DateFormat;

import java.time.LocalDateTime;
import java.util.UUID;


public record EmployeePlainDto(
  @NotNull UUID id,
  @NotBlank String employeeId,
  @NotBlank String firstname,
  @NotBlank String lastname,
  @NotBlank String middlename,
  @NotNull LocalDateTime dateOfBirth,
  @NotBlank String gender,
  @NotNull @PositiveOrZero Integer salary
) {

}
