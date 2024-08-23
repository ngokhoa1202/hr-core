package org.hr.employee.service;

import jakarta.persistence.NoResultException;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.JDBCException;
import org.hr.employee.dto.DepartmentCreationDTO;
import org.hr.employee.dto.DepartmentDTO;
import org.hr.employee.dto.DepartmentLocationCreationDTO;
import org.hr.employee.dto.DepartmentLocationDTO;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;

public interface DepartmentService {
  DepartmentDTO getDepartment(Long id) throws EntityNotFoundException;

  DepartmentDTO createDepartment(DepartmentCreationDTO departmentDTO)
    throws ConstraintViolationException, EntityNotFoundException, JDBCException;

  DepartmentDTO updateDepartment(Long id, DepartmentCreationDTO departmentDTO)
    throws JDBCException, EntityNotFoundException;

  void deleteDepartment(Long id) throws EntityNotFoundException;

  DepartmentLocationDTO getDepartmentLocation(Long id) throws EntityNotFoundException;

  DepartmentLocationDTO createDepartmentLocation(DepartmentLocationCreationDTO dto)
    throws EntityNotFoundException, NoResultException;

  DepartmentLocationDTO updateDepartmentLocation(Long id, DepartmentLocationCreationDTO dto)
    throws EntityNotFoundException, InvalidRequestBodyException;

  void deleteDepartmentLocation(Long id) throws EntityNotFoundException;
}
