package org.hr.employee.dto.project.assignment;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record AssignmentPlainDto(
  Long id,
  Integer numberOfHours
) implements Serializable {

}
