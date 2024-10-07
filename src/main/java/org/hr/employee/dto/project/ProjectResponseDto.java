package org.hr.employee.dto.project;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hr.employee.dto.department.DepartmentPlainDto;
import org.hr.employee.dto.project.assignment.AssignmentPlainDto;

import java.io.Serializable;
import java.util.List;

public record ProjectResponseDto(
  Long id,
  String name,
  String area,
  @JsonProperty("department") DepartmentPlainDto departmentPlainDto,
  @JsonProperty("assignments") List<AssignmentPlainDto> assignmentPlainDtos
) implements Serializable {

}
