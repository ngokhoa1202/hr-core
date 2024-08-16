package org.hr.employee.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.hr.employee.dao.AssignmentDAO;
import org.hr.employee.dao.EmployeeDAO;
import org.hr.employee.dao.ProjectDAO;
import org.hr.employee.dto.AssignmentCreationDTO;
import org.hr.employee.dto.AssignmentDTO;
import org.hr.employee.entity.Assignment;
import org.hr.employee.entity.Employee;
import org.hr.employee.entity.Project;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;

@ApplicationScoped
public class AssignmentServiceImpl implements AssignmentService {

  private final EmployeeDAO employeeDAO;
  private final AssignmentDAO assignmentDAO;
  private final ProjectDAO projectDAO;

  @Inject
  public AssignmentServiceImpl(
    @NonNull final AssignmentDAO assignmentDAO, @NonNull final EmployeeDAO employeeDAO,
    @NonNull final ProjectDAO projectDAO) {

    this.assignmentDAO = assignmentDAO;
    this.employeeDAO = employeeDAO;
    this.projectDAO = projectDAO;
  }

  @Override
  public AssignmentDTO getAssignment(Long id) {
    return this.assignmentDAO.findAssignmentById(id)
      .orElseThrow(() -> new EntityNotFoundException(Assignment.class.getName()))
      .toAssignmentDTO();
  }

  @Override
  @Transactional(value = Transactional.TxType.REQUIRED)
  public AssignmentDTO createAssignment(AssignmentCreationDTO assignmentCreationDTO) {

    Employee employeeAssigned = this.employeeDAO.findEmployeeById(assignmentCreationDTO.getEmployeeId())
      .orElseThrow(() -> new EntityNotFoundException(Employee.class.getSimpleName()));
    Project projectBelonging = this.projectDAO.findProjectById(assignmentCreationDTO.getProjectId())
      .orElseThrow(() -> new EntityNotFoundException(Project.class.getSimpleName()));

    return this.assignmentDAO.saveAssignment(
      assignmentCreationDTO.toAssignment()
        .setEmployeeAssigned(employeeAssigned)
        .setProjectBelonging(projectBelonging)
    ).orElseThrow(InvalidRequestBodyException::new).toAssignmentDTO();
  }

  @Override
  @Transactional
  public AssignmentDTO updateAssignment(Long id, AssignmentCreationDTO assignmentCreationDTO) {
    Assignment existedAssignment = this.assignmentDAO.findAssignmentById(id)
      .orElseThrow(() -> new EntityNotFoundException(Assignment.class.getSimpleName()));

    Employee employeeAssigned = this.employeeDAO.findEmployeeById(assignmentCreationDTO.getEmployeeId())
      .orElseThrow(() -> new EntityNotFoundException(Employee.class.getSimpleName()));
    Project projectBelonging = this.projectDAO.findProjectById(assignmentCreationDTO.getProjectId())
      .orElseThrow(() -> new EntityNotFoundException(Project.class.getSimpleName()));
    return this.assignmentDAO.updateAssignment(
      assignmentCreationDTO.toAssignment()
        .setId(id)
        .setEmployeeAssigned(employeeAssigned)
        .setProjectBelonging(projectBelonging)
    ).orElseThrow(InvalidRequestBodyException::new).toAssignmentDTO();
  }

  @Override
  @Transactional(value = Transactional.TxType.REQUIRED)
  public void deleteAssignment(Long id) throws EntityNotFoundException {
    this.assignmentDAO.deleteAssignment(id)
      .filter((rowsDeleted) -> rowsDeleted == 1)
      .orElseThrow(() -> new EntityNotFoundException(Assignment.class.getSimpleName()))
  }
}
