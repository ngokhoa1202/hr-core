package org.hr.employee.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record ProjectPlainDto(
  @NotNull Long id,
  @NotBlank String name,
  @NotBlank String area
) implements Serializable {

}
