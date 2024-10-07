package org.hr.employee.service;

import jakarta.validation.Valid;
import org.hr.employee.dto.department.location.DepartmentLocationPayloadDto;
import org.hr.employee.dto.department.location.DepartmentLocationResponseDTO;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.JDBCException;

import java.util.List;

public interface DepartmentLocationService {
  DepartmentLocationResponseDTO getDepartmentLocation(Long id) throws EntityNotFoundException;

  List<DepartmentLocationResponseDTO> getDepartmentLocations();

  DepartmentLocationResponseDTO createDepartmentLocation(@Valid DepartmentLocationPayloadDto dto)
    throws ConstraintViolationException, JDBCException, InvalidRequestBodyException;

  DepartmentLocationResponseDTO updateDepartmentLocation(Long id, @Valid DepartmentLocationPayloadDto dto)
    throws EntityNotFoundException, InvalidRequestBodyException, JDBCException;

  void deleteDepartmentLocation(Long id) throws EntityNotFoundException;
}
