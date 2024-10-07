package org.hr.employee.dto.department.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hr.employee.dto.department.DepartmentPlainDto;

import java.io.Serializable;

public record DepartmentLocationPayloadDto(
  @NotBlank String location,
  @NotNull @JsonProperty(value = "department") DepartmentPlainDto departmentPlainDto
) implements Serializable {

}
