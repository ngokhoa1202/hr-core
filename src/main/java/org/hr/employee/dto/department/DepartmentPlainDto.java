package org.hr.employee.dto.department;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hr.employee.entity.Department;

import java.time.LocalDateTime;

public record DepartmentPlainDto(
  Long id,
  String name,
  LocalDateTime startDate
) {

}
