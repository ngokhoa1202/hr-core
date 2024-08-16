package org.hr.employee.service;

import org.hr.employee.dto.AssignmentCreationDTO;
import org.hr.employee.dto.AssignmentDTO;

public interface AssignmentService {
  AssignmentDTO getAssignment(Long id) ;
  AssignmentDTO createAssignment(AssignmentCreationDTO assignmentCreationDTO);
  AssignmentDTO updateAssignment(Long id, AssignmentCreationDTO assignmentCreationDTO);
  void deleteAssignment(Long id);
}
