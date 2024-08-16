package org.hr.employee.service;

import org.hr.employee.dto.*;

/* TODO: Split into feature-based interfaces */
public interface EmployeeService {

  EmployeeDTO getEmployee(String id);
  EmployeeDTO saveEmployee(EmployeeCreationDTO employeeCreationDTO);
  EmployeeDTO updateEmployee(String employeeId, EmployeeCreationDTO employeeCreationDTO);
  void deleteEmployee(String employeeId);

}
