package org.hr.employee.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record DepartmentLocationDTO(
  Long id,
  String location,
  @JsonProperty(value = "department_id") Long departmentId) implements Serializable {

}
