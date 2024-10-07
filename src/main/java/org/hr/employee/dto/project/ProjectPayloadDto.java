package org.hr.employee.dto.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.hr.employee.dto.department.DepartmentPlainDto;
import org.hr.employee.entity.Project;

import java.io.Serializable;

public record ProjectPayloadDto(
  @NotBlank String name,
  @NotBlank String area,
  @JsonProperty(value = "department") DepartmentPlainDto departmentPlainDto
) implements Serializable {

}
