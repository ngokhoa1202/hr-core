package org.hr.employee.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.hibernate.JDBCException;
import org.hr.employee.dao.AssignmentDAO;
import org.hr.employee.dao.DepartmentDAO;
import org.hr.employee.dao.EmployeeDAO;
import org.hr.employee.dao.ProjectDAO;
import org.hr.employee.dto.*;
import org.hr.employee.entity.*;
import org.hr.exception.*;


@ApplicationScoped
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeDAO employeeDAO;
  private final DepartmentDAO departmentDAO;

  @Inject
  public EmployeeServiceImpl(EmployeeDAO employeeDAO, DepartmentDAO departmentDAO) {
    this.employeeDAO = employeeDAO;
    this.departmentDAO = departmentDAO;
  }

  @Override
  public EmployeeDTO getEmployee(String id) {
    return this.employeeDAO.findEmployeeById(id).orElseThrow(
      () -> new EntityNotFoundException(Employee.class.getName())
    ).toEmployeeDTO();
  }

  @Override
  @Transactional
  public EmployeeDTO saveEmployee(EmployeeCreationDTO employeeCreationDTO)
    throws EntityNotFoundException, ConstraintViolationException, JDBCException, InvalidRequestBodyException {

    Department department = this.departmentDAO.findDepartmentById(employeeCreationDTO.departmentId())
      .orElseThrow(() -> new EntityNotFoundException(Employee.class.getName()));

    return this.employeeDAO.saveEmployee(
      employeeCreationDTO.toEmployee().setDepartment(department)
    ).orElseThrow(InvalidRequestBodyException::new).toEmployeeDTO();
  }

  @Override
  @Transactional
  public EmployeeDTO updateEmployee(String employeeId, EmployeeCreationDTO employeeCreationDTO)
    throws EntityNotFoundException, ConstraintViolationException, JDBCException, InvalidRequestBodyException {

    Department existedDepartment = this.departmentDAO.findDepartmentById(employeeCreationDTO.departmentId())
      .orElseThrow(() -> new EntityNotFoundException(Department.class.getName()));

    Employee existedEmployee = this.employeeDAO.findEmployeeById(employeeId)
      .orElseThrow(() -> new EntityNotFoundException(Employee.class.getName()));
    return this.employeeDAO.updateEmployee(
      employeeCreationDTO.toEmployee().setDepartment(existedDepartment)
    ).orElseThrow(InvalidRequestBodyException::new).toEmployeeDTO();
  }

  @Override
  @Transactional
  public void deleteEmployee(String employeeId) throws EntityNotFoundException {
    this.employeeDAO.deleteEmployeeById(employeeId)
      .filter((rowsDeleted) -> rowsDeleted == 1)
      .orElseThrow(() -> new EntityNotFoundException(Employee.class.getName()));
  }

}

