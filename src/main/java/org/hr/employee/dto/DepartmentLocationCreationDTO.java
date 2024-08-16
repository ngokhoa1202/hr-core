package org.hr.employee.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hr.employee.entity.DepartmentLocation;

import java.io.Serializable;

public record DepartmentLocationCreationDTO(
  String location,
  @JsonProperty(value = "department_id") Long departmentId
) implements Serializable {

  public DepartmentLocation toDepartmentLocation() {
    return DepartmentLocation.builder().location(this.location).build();
  }
}
