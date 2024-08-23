package org.hr.employee.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;


public record DepartmentLocationDTO(
  Long id,
  String location,
  @JsonProperty(value = "department_id") Long departmentId) implements Serializable {


  @Override
  public boolean equals(Object o) {
    if (o == null || this.getClass() != o.getClass()) return false;
    DepartmentLocationDTO that = (DepartmentLocationDTO) o;
    return (
      Objects.equals(this.id, that.id) &&
      Objects.equals(this.location, that.location) &&
      Objects.equals(this.departmentId, that.departmentId)
    );
  }
}
