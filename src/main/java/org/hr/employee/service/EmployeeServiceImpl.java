package org.hr.employee.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.JDBCException;
import org.hr.employee.dao.DepartmentDao;
import org.hr.employee.dao.EmployeeDao;
import org.hr.employee.dto.department.DepartmentMapper;
import org.hr.employee.dto.employee.EmployeeAssignmentStatisticsDto;
import org.hr.employee.dto.employee.EmployeeMapper;
import org.hr.employee.dto.employee.EmployeeResponseDto;
import org.hr.employee.dto.employee.EmployeePayloadDto;
import org.hr.employee.entity.*;
import org.hr.exception.*;
import org.hr.employee.dto.TotalNumberDTO;

import java.util.List;
import java.util.Locale;
import java.util.UUID;


@ApplicationScoped
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeDao employeeDAO;
  private final DepartmentDao departmentDAO;

  @Override
  public EmployeeResponseDto getEmployee(UUID id) {

    Employee employee = this.employeeDAO.findById(id)
      .orElseThrow(() ->  new EntityNotFoundException("id", Employee.class.getSimpleName()));

    return EmployeeMapper.INSTANCE.employeeToEmployeeResponseDto(employee);
  }

  @Override
  public EmployeeResponseDto createEmployee(@Valid EmployeePayloadDto employeePayloadDto)
    throws EntityNotFoundException, ConstraintViolationException, JDBCException, InvalidRequestBodyException {

    Employee employee = EmployeeMapper.INSTANCE.employeePayloadDtoToEmployee(employeePayloadDto);
    Department departmentById = this.departmentDAO.findById(employeePayloadDto.departmentPlainDto().id())
      .orElseThrow(() -> new EntityNotFoundException("id", Department.class.getSimpleName()));
    Department departmentByDto = employee.getDepartment();
    DepartmentService.ensureDepartmentIntegrity(departmentById, departmentByDto);

    employee.setDepartment(departmentById);
    departmentById.getEmployees().add(employee);

    Employee employeeCreated = this.employeeDAO.create(employee)
      .orElseThrow(InvalidRequestBodyException::new);
    return EmployeeMapper.INSTANCE.employeeToEmployeeResponseDto(employeeCreated);
  }

  @Override
  public EmployeeResponseDto updateEmployee(UUID id, @Valid EmployeePayloadDto employeePayloadDTO)
    throws EntityNotFoundException, ConstraintViolationException, JDBCException, InvalidRequestBodyException {


    Department newDepartmentById = this.departmentDAO.findById(employeePayloadDTO.departmentPlainDto().id())
      .orElseThrow(() -> new EntityNotFoundException("id", Department.class.getSimpleName()));
    Department newDepartmentByDto = DepartmentMapper.INSTANCE.departmentPlainDtoToDepartment(employeePayloadDTO.departmentPlainDto());
    DepartmentService.ensureDepartmentIntegrity(newDepartmentById, newDepartmentByDto);

    Employee employee = this.employeeDAO.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("id", Employee.class.getSimpleName()));

    employee.setFirstname(employeePayloadDTO.firstname());
    employee.setLastname(employeePayloadDTO.lastname());
    employee.setMiddlename(employee.getMiddlename());
    employee.setDateOfBirth(employeePayloadDTO.dateOfBirth());
    employee.setGender(GenderEnum.valueOf(employeePayloadDTO.gender().toUpperCase(Locale.ROOT)));
    employee.setSalary(employeePayloadDTO.salary());

    Department oldDepartment = employee.getDepartment();
    oldDepartment.getEmployees().remove(employee);
    employee.setDepartment(newDepartmentById);

    Employee employeeUpdated = this.employeeDAO.update(employee)
      .orElseThrow(InvalidRequestBodyException::new);
    return EmployeeMapper.INSTANCE.employeeToEmployeeResponseDto(employeeUpdated);
  }

  @Override
  public void deleteEmployee(UUID id) throws EntityNotFoundException {
    this.employeeDAO.deleteById(id);
  }


  @Override
  public List<EmployeeResponseDto> getEmployees(int startIndex, int limit) {
    return this.employeeDAO.findAll(startIndex, limit)
      .map(EmployeeMapper.INSTANCE::employeeToEmployeeResponseDto)
      .toList();
  }

  @Override
  public TotalNumberDTO getTotalNumberOfEmployees() {
    return new TotalNumberDTO(this.employeeDAO.countTotal());
  }

  @Override
  public List<EmployeeResponseDto> getEmployeesByEmployeeId(String employeeId, int startIndex, int limit) {
    return this.employeeDAO.findByEmployeeIdPrefix(employeeId, startIndex, limit)
      .map(EmployeeMapper.INSTANCE::employeeToEmployeeResponseDto)
      .toList();
  }

  @Override
  public List<EmployeeResponseDto> getEmployeesByName(String name, int startIndex, int limit) {
    return this.employeeDAO.findByFirstnamePrefixOrLastnamePrefixOrMiddlePrefix(name.toLowerCase(Locale.ROOT), startIndex, limit)
      .map(EmployeeMapper.INSTANCE::employeeToEmployeeResponseDto)
      .toList();
  }

  @Override
  public EmployeeAssignmentStatisticsDto getEmployeeWithAssignmentStatistics(UUID id) throws EntityNotFoundException {

    return this.employeeDAO.findAssignmentStatisticsById(id)
      .findAny().orElseThrow(() -> new EntityNotFoundException("id", Employee.class.getSimpleName()));
  }
}

