package org.hr.employee.dto.department;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

public record DepartmentEmployeeStatisticsDto(
  Long id,
  String name,
  LocalDateTime startDate,
  Long numberOfEmployees,
  Long totalSalary,
  Long minimumSalary,
  Long maximumSalary,
  Double averageSalary
) implements Serializable {

}
