package org.hr.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hr.employee.entity.Assignment;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class AssignmentCreationDTO implements Serializable {

  private final int numberOfHours;
  private String employeeId;
  private Long projectId;

  public Assignment toAssignment() {
    return Assignment.builder().numberOfHours(this.numberOfHours).build();
  }

  public AssignmentCreationDTO setProjectId(Long projectId) {
    this.projectId = projectId;
    return this;
  }

  public AssignmentCreationDTO setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
    return this;
  }
}
