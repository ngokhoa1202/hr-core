package org.hr.employee.scenario;

import org.hr.employee.dto.department.DepartmentPayloadDto;
import org.hr.employee.dto.department.DepartmentResponseDto;
import org.hr.employee.dto.department.location.DepartmentLocationPayloadDto;
import org.hr.employee.dto.department.location.DepartmentLocationResponseDTO;
import org.hr.employee.entity.Department;

import java.util.Optional;

public interface DepartmentTestScenario {

  org.hibernate.exception.ConstraintViolationException mockHibernateUniqueViolationException(String constraintName);
  DepartmentResponseDto mockDepartmentResponseDto();
  String mockInvalidJwtToken();
  DepartmentPayloadDto mockDepartmentCreationDTO();
  DepartmentLocationPayloadDto mockDepartmentLocationCreationDTOWithInvalidLocation();
  Long mockDepartmentId();
  Long mockDepartmentLocationId();
  DepartmentLocationPayloadDto mockDepartmentLocationCreationDTO();
  DepartmentLocationResponseDTO mockDepartmentLocationDTO();
  jakarta.validation.ConstraintViolationException mockJakartaConstraintViolationException(Object o);
  DepartmentPayloadDto mockDepartmentCreationDTOWithInvalidName();

  Optional<Department> mockOptionalDepartment();
}
