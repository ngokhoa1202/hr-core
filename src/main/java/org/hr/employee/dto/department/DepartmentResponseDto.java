package org.hr.employee.dto.department;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hr.employee.dto.department.location.DepartmentLocationPlainDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record DepartmentResponseDto(
  Long id,
  String name,
  LocalDateTime startDate,
  List<DepartmentLocationPlainDto> locationPlainDtos
) implements Serializable {


}
