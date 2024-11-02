package org.hr.employee.dto.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

public record DepartmentPayloadDto(
  @NotBlank String name,
  @NotNull LocalDateTime startDate
) implements Serializable {

}
