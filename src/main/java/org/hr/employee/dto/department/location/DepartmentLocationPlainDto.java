package org.hr.employee.dto.department.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DepartmentLocationPlainDto(
  @NotNull Long id,
  @NotBlank String location
) {
}
