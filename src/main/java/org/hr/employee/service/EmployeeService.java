package org.hr.employee.service;

import jakarta.validation.ConstraintViolationException;
import org.hibernate.JDBCException;
import org.hr.employee.dto.*;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;

/* TODO: Split into feature-based interfaces */
public interface EmployeeService {

  EmployeeDTO getEmployee(String id) throws EntityNotFoundException;

  EmployeeDTO saveEmployee(EmployeeCreationDTO employeeCreationDTO)
    throws EntityNotFoundException, ConstraintViolationException, JDBCException, InvalidRequestBodyException;

  EmployeeDTO updateEmployee(String employeeId, EmployeeCreationDTO employeeCreationDTO)
    throws EntityNotFoundException, ConstraintViolationException, JDBCException, InvalidRequestBodyException;

  void deleteEmployee(String employeeId) throws EntityNotFoundException;

}
