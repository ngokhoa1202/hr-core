package org.hr.employee.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.JDBCException;
import org.hr.employee.dao.AssignmentDao;
import org.hr.employee.dao.EmployeeDao;
import org.hr.employee.dao.ProjectDao;
import org.hr.employee.dto.employee.EmployeeMapper;
import org.hr.employee.dto.project.ProjectMapper;
import org.hr.employee.dto.project.assignment.AssignmentMapper;
import org.hr.employee.dto.project.assignment.AssignmentPayloadDto;
import org.hr.employee.dto.project.assignment.AssignmentResponseDto;
import org.hr.employee.entity.Assignment;
import org.hr.employee.entity.Employee;
import org.hr.employee.entity.Project;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;

@ApplicationScoped
@RequiredArgsConstructor
@Transactional
public class AssignmentServiceImpl implements AssignmentService {

  private final EmployeeDao employeeDAO;
  private final AssignmentDao assignmentDAO;
  private final ProjectDao projectDAO;


  @Override
  public AssignmentResponseDto getAssignment(Long id) throws EntityNotFoundException {
    Assignment assignment = this.assignmentDAO.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("id", Assignment.class.getName()));
    return AssignmentMapper.INSTANCE.assignmentToAssignmentResponseDto(assignment);
  }

  @Override
  public AssignmentResponseDto createAssignment(@Valid AssignmentPayloadDto assignmentPayloadDto)
    throws EntityNotFoundException, ConstraintViolationException, JDBCException, InvalidRequestBodyException {

    Employee employeeAssignedById = this.employeeDAO.findById(assignmentPayloadDto.employeePlainDto().id())
      .orElseThrow(() -> new EntityNotFoundException("id", Employee.class.getSimpleName()));
    Employee employeeAssignedByDto = EmployeeMapper.INSTANCE.employeePlainDtoToEmployee(assignmentPayloadDto.employeePlainDto());
    EmployeeService.ensureEmployeeIntegrity(employeeAssignedById, employeeAssignedByDto);

    Project projectBelongingById = this.projectDAO.findById(assignmentPayloadDto.projectPlainDTO().id())
      .orElseThrow(() -> new EntityNotFoundException("id", Project.class.getSimpleName()));
    Project projectBelongingByDto = ProjectMapper.INSTANCE.projectPlainDtoToProject(assignmentPayloadDto.projectPlainDTO());
    ProjectService.ensureProjectIntegrity(projectBelongingById, projectBelongingByDto);

    Assignment assignment = AssignmentMapper.INSTANCE.assignmentPayloadDtoToAssignment(assignmentPayloadDto);
    assignment.setEmployeeAssigned(employeeAssignedById);
    assignment.setProjectBelonging(projectBelongingById);
    employeeAssignedById.getAssignments().add(assignment);
    projectBelongingById.getAssignments().add(assignment);

    Assignment assignmentCreated = this.assignmentDAO.create(assignment)
      .orElseThrow(InvalidRequestBodyException::new);
    return AssignmentMapper.INSTANCE.assignmentToAssignmentResponseDto(assignmentCreated);
  }

  @Override
  public AssignmentResponseDto updateAssignment(Long id, @Valid AssignmentPayloadDto assignmentPayloadDto)
    throws EntityNotFoundException, ConstraintViolationException, JDBCException, InvalidRequestBodyException {

    Assignment assignment = this.assignmentDAO.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("id", Assignment.class.getSimpleName()));

    Employee newEmployeeById = this.employeeDAO.findById(assignmentPayloadDto.employeePlainDto().id())
      .orElseThrow(() -> new EntityNotFoundException("id", Employee.class.getSimpleName()));
    Employee newEmployeeByDto = EmployeeMapper.INSTANCE.employeePlainDtoToEmployee(assignmentPayloadDto.employeePlainDto());
    EmployeeService.ensureEmployeeIntegrity(newEmployeeById, newEmployeeByDto);

    Project newProjectById = this.projectDAO.findById(assignmentPayloadDto.projectPlainDTO().id())
      .orElseThrow(() -> new EntityNotFoundException("id", Project.class.getSimpleName()));
    Project newProjectByDto = ProjectMapper.INSTANCE.projectPlainDtoToProject(assignmentPayloadDto.projectPlainDTO());
    ProjectService.ensureProjectIntegrity(newProjectById, newProjectByDto);

    assignment.setNumberOfHours(assignmentPayloadDto.numberOfHours());
    assignment.setEmployeeAssigned(newEmployeeById);
    assignment.setProjectBelonging(newProjectById);

    Employee oldEmployee = assignment.getEmployeeAssigned();
    oldEmployee.getAssignments().remove(assignment);
    Project oldProject = assignment.getProjectBelonging();
    oldProject.getAssignments().remove(assignment);

    Assignment assignmentUpdated = this.assignmentDAO.update(assignment)
      .orElseThrow(InvalidRequestBodyException::new);
    return AssignmentMapper.INSTANCE.assignmentToAssignmentResponseDto(assignmentUpdated);
  }

  @Override
  public void deleteAssignment(Long id) throws EntityNotFoundException {
    this.assignmentDAO.deleteById(id);
  }
}
