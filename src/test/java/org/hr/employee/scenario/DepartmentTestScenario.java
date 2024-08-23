package org.hr.employee.scenario;

import org.hr.employee.dto.DepartmentCreationDTO;
import org.hr.employee.dto.DepartmentDTO;
import org.hr.employee.dto.DepartmentLocationCreationDTO;
import org.hr.employee.dto.DepartmentLocationDTO;
import org.hr.employee.entity.Department;
import org.hr.employee.entity.DepartmentLocation;

import java.util.Optional;

public interface DepartmentTestScenario {

  org.hibernate.exception.ConstraintViolationException mockHibernateUniqueViolationException(String constraintName);
  DepartmentDTO mockDepartmentDTO();
  String mockInvalidJwtToken();
  DepartmentCreationDTO mockDepartmentCreationDTO();
  DepartmentLocationCreationDTO mockDepartmentLocationCreationDTOWithInvalidLocation();
  Long mockDepartmentId();
  Long mockDepartmentLocationId();
  DepartmentLocationCreationDTO mockDepartmentLocationCreationDTO();
  DepartmentLocationDTO mockDepartmentLocationDTO();
  jakarta.validation.ConstraintViolationException mockJakartaConstraintViolationException(Object o);
  DepartmentCreationDTO mockDepartmentCreationDTOWithInvalidName();

  Optional<Department> mockOptionalDepartment();
}
