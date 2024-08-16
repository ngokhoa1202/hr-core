package org.hr.employee.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record ProjectDTO (
  @JsonProperty("project_id") Long id,
  String name,
  String area,
  @JsonProperty(value = "department_id") Long departmentId
) implements Serializable {

}
