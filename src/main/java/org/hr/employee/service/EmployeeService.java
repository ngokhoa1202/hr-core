package org.hr.employee.service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.hibernate.JDBCException;
import org.hr.employee.dto.employee.EmployeeAssignmentStatisticsDto;
import org.hr.employee.dto.employee.EmployeeResponseDto;
import org.hr.employee.dto.employee.EmployeePayloadDto;
import org.hr.employee.entity.Employee;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;
import org.hr.employee.dto.TotalNumberDTO;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

  EmployeeResponseDto getEmployee(UUID id) throws EntityNotFoundException;

  List<EmployeeResponseDto> getEmployees(int startIndex, int limit);

  List<EmployeeResponseDto> getEmployeesByEmployeeId(String employeeId, int startIndex, int limit);

  List<EmployeeResponseDto> getEmployeesByName(String name, int startIndex, int limit);

  EmployeeAssignmentStatisticsDto getEmployeeWithAssignmentStatistics(UUID id) throws EntityNotFoundException;

  EmployeeResponseDto createEmployee(@Valid EmployeePayloadDto employeePayloadDTO)
    throws EntityNotFoundException, ConstraintViolationException, JDBCException, InvalidRequestBodyException;

  EmployeeResponseDto updateEmployee(UUID id, @Valid EmployeePayloadDto employeePayloadDTO)
    throws EntityNotFoundException, ConstraintViolationException, JDBCException, InvalidRequestBodyException;

  TotalNumberDTO getTotalNumberOfEmployees();

  void deleteEmployee(UUID id) throws EntityNotFoundException;

  static void ensureEmployeeIntegrity(Employee persistentEmployee, Employee detachedEmployee) {
    if (! persistentEmployee.getEmployeeId().equals(detachedEmployee.getEmployeeId())) {
      throw new InvalidRequestBodyException("employee_id", Employee.class.getSimpleName());
    }

    if (! persistentEmployee.getFirstname().equals(detachedEmployee.getFirstname())) {
      throw new InvalidRequestBodyException("first_name", Employee.class.getSimpleName());
    }

    if (! persistentEmployee.getLastname().equals(detachedEmployee.getLastname())) {
      throw new InvalidRequestBodyException("last_name", Employee.class.getSimpleName());
    }

    if (! persistentEmployee.getMiddlename().equals(detachedEmployee.getMiddlename())) {
      throw new InvalidRequestBodyException("middle_name", Employee.class.getSimpleName());
    }

    if (! persistentEmployee.getSalary().equals(detachedEmployee.getSalary())) {
      throw new InvalidRequestBodyException("salary", Employee.class.getSimpleName());
    }

    if (! persistentEmployee.getDateOfBirth().toLocalDate().isEqual(detachedEmployee.getDateOfBirth().toLocalDate())) {
      throw new InvalidRequestBodyException("date_of_birth", Employee.class.getSimpleName());
    }

    if (! persistentEmployee.getGender().equals(detachedEmployee.getGender())) {
      throw new InvalidRequestBodyException("gender", Employee.class.getSimpleName());
    }
  }

}
