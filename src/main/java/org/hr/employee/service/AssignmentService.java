package org.hr.employee.service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.hibernate.JDBCException;
import org.hr.employee.dto.project.assignment.AssignmentPayloadDto;
import org.hr.employee.dto.project.assignment.AssignmentResponseDto;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;

public interface AssignmentService {

  AssignmentResponseDto getAssignment(Long id) throws EntityNotFoundException;

  AssignmentResponseDto createAssignment(@Valid AssignmentPayloadDto assignmentCreationDTO)
    throws EntityNotFoundException, ConstraintViolationException, JDBCException, InvalidRequestBodyException;

  AssignmentResponseDto updateAssignment(Long id, @Valid AssignmentPayloadDto assignmentCreationDTO)
    throws EntityNotFoundException, ConstraintViolationException, JDBCException, InvalidRequestBodyException;

  void deleteAssignment(Long id) throws EntityNotFoundException;
}
