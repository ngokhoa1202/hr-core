package org.hr.employee.service;

import jakarta.validation.ConstraintViolationException;
import org.hibernate.JDBCException;
import org.hr.employee.dto.AssignmentCreationDTO;
import org.hr.employee.dto.AssignmentDTO;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;

public interface AssignmentService {

  AssignmentDTO getAssignment(Long id) throws EntityNotFoundException;

  AssignmentDTO createAssignment(AssignmentCreationDTO assignmentCreationDTO)
    throws EntityNotFoundException, ConstraintViolationException, JDBCException, InvalidRequestBodyException;

  AssignmentDTO updateAssignment(Long id, AssignmentCreationDTO assignmentCreationDTO)
    throws EntityNotFoundException, ConstraintViolationException, JDBCException, InvalidRequestBodyException;

  void deleteAssignment(Long id) throws EntityNotFoundException;
}
