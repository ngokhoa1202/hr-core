package org.hr.employee.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.hr.employee.entity.Department;

import java.sql.Date;

public record DepartmentCreationDTO(
  @NotEmpty String name,
  @JsonProperty(value = "start_date") Date startDate
) {

  public Department toDepartment() {
    return Department.builder().name(this.name).startDate(this.startDate).build();
  }

}
