package org.hr.employee.service;

import jakarta.validation.ConstraintViolationException;
import org.hibernate.JDBCException;
import org.hr.employee.dto.DepartmentCreationDTO;
import org.hr.employee.dto.DepartmentDTO;
import org.hr.employee.dto.DepartmentLocationCreationDTO;
import org.hr.employee.dto.DepartmentLocationDTO;
import org.hr.exception.EntityNotFoundException;

public interface DepartmentService {
  DepartmentDTO getDepartment(Long id) throws EntityNotFoundException;

  DepartmentDTO saveDepartment(DepartmentCreationDTO departmentDTO)
    throws ConstraintViolationException, EntityNotFoundException, JDBCException;

  DepartmentDTO updateDepartment(Long id, DepartmentCreationDTO departmentDTO)
    throws JDBCException, EntityNotFoundException;

  void deleteDepartment(Long id) throws EntityNotFoundException;

  DepartmentLocationDTO getDepartmentLocation(Long id);

  DepartmentLocationDTO saveDepartmentLocation(DepartmentLocationCreationDTO dto);

  DepartmentLocationDTO updatedDepartmentLocation(Long id, DepartmentLocationCreationDTO dto);

  void deleteDepartmentLocation(Long id) throws EntityNotFoundException;
}
