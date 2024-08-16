package org.hr.employee.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.hibernate.JDBCException;
import org.hr.employee.dao.DepartmentDAO;
import org.hr.employee.dao.ProjectDAO;
import org.hr.employee.dto.ProjectCreationDTO;
import org.hr.employee.dto.ProjectDTO;
import org.hr.employee.entity.Department;
import org.hr.employee.entity.Project;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;

@ApplicationScoped
public class ProjectServiceImpl implements ProjectService {

  private final ProjectDAO projectDAO;
  private final DepartmentDAO departmentDAO;

  @Inject
  public ProjectServiceImpl(ProjectDAO projectDAO, DepartmentDAO departmentDAO) {
    this.projectDAO = projectDAO;
    this.departmentDAO = departmentDAO;
  }

  @Override
  public ProjectDTO getProject(Long id) throws EntityNotFoundException, ConstraintViolationException, JDBCException {
    return this.projectDAO.findProjectById(id)
      .orElseThrow(() -> new EntityNotFoundException(Project.class.getName()))
      .toProjectDTO();
  }

  @Override
  @Transactional
  public ProjectDTO saveProject(ProjectCreationDTO projectCreationDTO)
    throws EntityNotFoundException {

    Department existedDepartment = this.departmentDAO.findDepartmentById(projectCreationDTO.departmentId())
      .orElseThrow(() -> new EntityNotFoundException(Department.class.getName()));
    return this.projectDAO.saveProject(
      projectCreationDTO.toProject().setManagedDepartment(existedDepartment)
    ).orElseThrow(InvalidRequestBodyException::new).toProjectDTO();
  }

  @Override
  @Transactional
  public ProjectDTO updateProject(Long projectId, ProjectCreationDTO projectCreationDTO)
    throws EntityNotFoundException, ConstraintViolationException, JDBCException {

    Department existedDepartment = this.departmentDAO.findDepartmentById(projectCreationDTO.departmentId())
      .orElseThrow(() -> new EntityNotFoundException(Department.class.getName()));

    Project project = projectCreationDTO.toProject().setId(projectId).setManagedDepartment(existedDepartment);
    return this.projectDAO.saveProject(project)
      .orElseThrow(InvalidRequestBodyException::new)
      .toProjectDTO();
  }

  @Override
  @Transactional
  public void deleteProject(Long projectId) throws EntityNotFoundException {
    this.projectDAO.deleteProject(projectId)
      .filter((rowsDeleted) -> rowsDeleted == 1)
      .orElseThrow(() -> new EntityNotFoundException(Project.class.getName()));
  }


}
