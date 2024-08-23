package org.hr.employee.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.hr.employee.entity.Department;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public record DepartmentDTO(
  Long id,
  String name,
  @JsonProperty(value = "start_date") Date startDate) implements Serializable {

  public Department toDepartment() {
    return Department.builder().id(this.id).name(this.name).startDate(this.startDate).build();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || this.getClass() != o.getClass()) return false;
    DepartmentDTO that = (DepartmentDTO) o;
    return (
      Objects.equals(id, that.id) &&
      Objects.equals(name, that.name) &&
      Objects.equals(this.startDate.toString(), that.startDate.toString())
    );
  }

}
