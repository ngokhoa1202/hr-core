package org.hr.employee.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hr.employee.entity.Project;

import java.io.Serializable;

public record ProjectCreationDTO(
  String name,
  String area,
  @JsonProperty(value = "department_id") Long departmentId
) implements Serializable {

  public Project toProject() {
    return Project.builder().name(this.name).area(this.area).build();
  }
}
