package org.hr.employee.service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.hibernate.JDBCException;
import org.hr.employee.dto.*;
import org.hr.employee.dto.department.DepartmentEmployeeStatisticsDto;
import org.hr.employee.dto.department.DepartmentPayloadDto;
import org.hr.employee.dto.department.DepartmentResponseDto;
import org.hr.employee.dto.employee.EmployeeDepartmentAssignmentStatisticsDto;
import org.hr.employee.dto.employee.EmployeeResponseDto;
import org.hr.employee.entity.Department;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;

import java.util.List;

public interface DepartmentService {
  DepartmentResponseDto getDepartment(Long id) throws EntityNotFoundException;

  List<DepartmentResponseDto> getDepartments();

  DepartmentEmployeeStatisticsDto getDepartmentWithEmployeeStatistics(Long id) throws EntityNotFoundException;

  TotalNumberDTO getTotalNumberOfDepartments();

  List<EmployeeResponseDto> getEmployeesWithSalaryGreaterOrEqualToGivenSalaryWithinDepartment(Long id, int salary, int limit)
    throws EntityNotFoundException;

  List<EmployeeDepartmentAssignmentStatisticsDto> getEmployeesWithLowestHoursSpentPerAssignmentWithinDepartmentInDescendingOrder(Long id, int limit)
    throws EntityNotFoundException;

  List<EmployeeDepartmentAssignmentStatisticsDto> getEmployeesWithLowestHoursSpentPerAssignmentWithinDepartmentInAscendingOrder(Long id, int limit)
    throws EntityNotFoundException;

  DepartmentResponseDto createDepartment(@Valid DepartmentPayloadDto departmentDTO)
    throws ConstraintViolationException, EntityNotFoundException, JDBCException;

  DepartmentResponseDto updateDepartment(Long id, @Valid DepartmentPayloadDto departmentDTO)
    throws JDBCException, EntityNotFoundException;

  void deleteDepartment(Long id) throws EntityNotFoundException;

  static void ensureDepartmentIntegrity(Department persistentDepartment, Department detachedDepartment)
    throws InvalidRequestBodyException {


    if (! persistentDepartment.getName().equals(detachedDepartment.getName())) {
      throw new InvalidRequestBodyException("name", Department.class.getSimpleName());
    }

    if (! persistentDepartment.getStartDate().toLocalDate().isEqual(detachedDepartment.getStartDate().toLocalDate())) {
      throw new InvalidRequestBodyException("start_date", Department.class.getSimpleName());
    }
  }
}

