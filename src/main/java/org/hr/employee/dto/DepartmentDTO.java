package org.hr.employee.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hr.employee.entity.Department;

import java.io.Serializable;
import java.sql.Date;

public record DepartmentDTO(
  Long id,
  String name,
  @JsonProperty(value = "start_date") Date startDate) implements Serializable {

  public Department toDepartment() {
    return Department.builder().id(this.id).name(this.name).startDate(this.startDate).build();
  }
}
