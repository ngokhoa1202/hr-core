package org.hr.employee.service;

import io.quarkus.logging.Log;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.JDBCException;
import org.hr.employee.cache.CacheService;
import org.hr.employee.dao.DepartmentDao;
import org.hr.employee.dto.*;
import org.hr.employee.dto.department.*;
import org.hr.employee.dto.employee.EmployeeDepartmentAssignmentStatisticsDto;
import org.hr.employee.dto.employee.EmployeeMapper;
import org.hr.employee.dto.employee.EmployeeResponseDto;
import org.hr.employee.entity.Department;
import jakarta.validation.ConstraintViolationException;
import org.hr.exception.*;
import java.util.Comparator;
import java.util.List;


@ApplicationScoped
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentDao departmentDAO;
  private final CacheService cacheService;

  @PostConstruct
  public void initialize() {
    this.cacheService.cacheDepartments();
  }

  @Override
  public DepartmentResponseDto createDepartment(@Valid DepartmentPayloadDto departmentPayloadDto)
    throws ConstraintViolationException, InvalidRequestBodyException, JDBCException {

    Department department = DepartmentMapper.INSTANCE.departmentPayloadDtoToDepartment(departmentPayloadDto);

    Department departmentCreated =  this.departmentDAO.create(department)
      .orElseThrow(InvalidRequestBodyException::new);
    this.cacheService.evictCachedDepartments();
    return DepartmentMapper.INSTANCE.departmentToDepartmentResponseDto(departmentCreated);
  }

  @Override
  public void deleteDepartment(@NonNull Long id) throws EntityNotFoundException {

    this.departmentDAO.deleteById(id);
    this.cacheService.evictCachedDepartments();
  }

  @Override
  public DepartmentResponseDto updateDepartment(
    @NonNull Long id, @Valid DepartmentPayloadDto departmentPayloadDTO
  ) throws EntityNotFoundException, JDBCException, InvalidRequestBodyException {

    Department department = this.departmentDAO.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("id", Department.class.getSimpleName()));

    department.setName(departmentPayloadDTO.name());
    department.setStartDate(departmentPayloadDTO.startDate());

    Department departmentUpdated = this.departmentDAO.update(department)
      .orElseThrow(InvalidRequestBodyException::new);

    this.cacheService.evictCachedDepartments();
    return DepartmentMapper.INSTANCE.departmentToDepartmentResponseDto(departmentUpdated);
  }

  @Override
  public DepartmentResponseDto getDepartment(@NonNull Long id) throws EntityNotFoundException {
    Department department = this.cacheService.getCachedDepartmentById(id)
      .orElseThrow(() -> new EntityNotFoundException("id", Department.class.getName()));
    return DepartmentMapper.INSTANCE.departmentToDepartmentResponseDto(department);
  }

  @Override
  @Transactional
  public List<DepartmentResponseDto> getDepartments() {
    return this.cacheService.getCachedDepartments().values()
      .stream()
      .map(DepartmentMapper.INSTANCE::departmentToDepartmentResponseDto)
      .toList();
  }

  @Override
  public TotalNumberDTO getTotalNumberOfDepartments() {
    return new TotalNumberDTO((long) this.cacheService.getCachedDepartments().size());
  }

  @Override
  public DepartmentEmployeeStatisticsDto getDepartmentWithEmployeeStatistics(Long id) throws EntityNotFoundException {

    return this.departmentDAO.findEmployeeStatisticsById(id)
      .findAny().orElseThrow(() -> new EntityNotFoundException("id", Department.class.getSimpleName()));
  }

  /**
   * Find employees whose salary is greater or equal to a given salary
   * The predicate is implemented on the stream api on service layer, not the database layer
   */
  @Override
  public List<EmployeeResponseDto> getEmployeesWithSalaryGreaterOrEqualToGivenSalaryWithinDepartment(
    Long id, int salary, int limit
  ) throws EntityNotFoundException {

    Department department = this.departmentDAO.findWithEmployeesById(id)
      .orElseThrow(() -> new EntityNotFoundException("id", Department.class.getSimpleName()));

    return department.getEmployees().parallelStream()
      .filter((employee) -> employee.getSalary() >= salary)
      .map(EmployeeMapper.INSTANCE::employeeToEmployeeResponseDto)
      .limit(limit)
      .sorted(Comparator.comparingInt(EmployeeResponseDto::salary))
      .toList();
  }

  /**
   * Find employees that has the lowest hours spent per numberOfAssignments - the highest performance
   * The employee who has not joined any project yet will not be queried
   *
   */
  @Override
  public List<EmployeeDepartmentAssignmentStatisticsDto> getEmployeesWithLowestHoursSpentPerAssignmentWithinDepartmentInDescendingOrder(
    Long id, int limit
  ) throws EntityNotFoundException {

    return this.departmentDAO
      .findEmployeesWithLowestHoursSpentPerAssignmentInDescendingOrderById(id, limit)
      .map(EmployeeMapper.INSTANCE::tupleToEmployeeDepartmentAssignmentStatisticsDto)
      .toList();
  }

  @Override
  public List<EmployeeDepartmentAssignmentStatisticsDto> getEmployeesWithLowestHoursSpentPerAssignmentWithinDepartmentInAscendingOrder(
    Long id, int limit
  ) throws EntityNotFoundException {

    return this.departmentDAO.findEmployeesWithLowestHoursSpentPerAssignmentInAscendingOrderById(id, limit)
      .map(EmployeeMapper.INSTANCE::tupleToEmployeeDepartmentAssignmentStatisticsDto)
      .toList();
  }
}
