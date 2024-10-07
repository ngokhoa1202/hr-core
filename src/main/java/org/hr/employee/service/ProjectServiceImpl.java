package org.hr.employee.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.JDBCException;
import org.hr.employee.cache.CacheService;
import org.hr.employee.dao.DepartmentDao;
import org.hr.employee.dao.ProjectDao;
import org.hr.employee.dto.department.DepartmentMapper;
import org.hr.employee.dto.project.ProjectAssignmentStatisticsDto;
import org.hr.employee.dto.project.ProjectMapper;
import org.hr.employee.dto.project.ProjectPayloadDto;
import org.hr.employee.dto.project.ProjectResponseDto;
import org.hr.employee.entity.Department;
import org.hr.employee.entity.Project;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

  private final ProjectDao projectDao;
  private final DepartmentDao departmentDao;
  private final CacheService cacheService;

  @PostConstruct
  public void initialize() {
    this.cacheService.cacheProjects();
  }

  @Override
  public ProjectResponseDto getProject(Long id) throws EntityNotFoundException, ConstraintViolationException, JDBCException {
    Project project = this.cacheService.getCachedProjectById(id)
      .orElseThrow(() -> new EntityNotFoundException("id", Project.class.getSimpleName()));

    return ProjectMapper.INSTANCE.projectToProjectResponseDto(project);
  }

  @Override
  public ProjectResponseDto createProject(@Valid ProjectPayloadDto projectPayloadDto)
    throws EntityNotFoundException {

    Project project = ProjectMapper.INSTANCE.projectPayloadDtoToProject(projectPayloadDto);
    Department departmentById = this.departmentDao.findById(projectPayloadDto.departmentPlainDto().id())
      .orElseThrow(() -> new EntityNotFoundException("id", Department.class.getSimpleName()));
    Department departmentByDto = project.getManagedDepartment();
    DepartmentService.ensureDepartmentIntegrity(departmentById, departmentByDto);

    project.setManagedDepartment(departmentById);
    departmentById.getProjects().add(project);

    Project projectCreated =  this.projectDao.create(project)
      .orElseThrow(InvalidRequestBodyException::new);
    this.cacheService.evictCachedProjects();
    return ProjectMapper.INSTANCE.projectToProjectResponseDto(projectCreated);
  }

  @Override
  public ProjectResponseDto updateProject(Long id, @Valid ProjectPayloadDto projectCreationDTO)
    throws EntityNotFoundException, ConstraintViolationException, JDBCException {

    Department newDepartmentById = this.departmentDao.findById(projectCreationDTO.departmentPlainDto().id())
      .orElseThrow(() -> new EntityNotFoundException("id", Department.class.getName()));
    Department newDepartmentByDto = DepartmentMapper.INSTANCE.departmentPlainDtoToDepartment(projectCreationDTO.departmentPlainDto());
    DepartmentService.ensureDepartmentIntegrity(newDepartmentById, newDepartmentByDto);

    Project project = this.projectDao.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("id", Project.class.getSimpleName()));

    project.setName(projectCreationDTO.name());
    project.setArea(projectCreationDTO.area());
    project.setManagedDepartment(newDepartmentById);
    Department oldDepartment = project.getManagedDepartment();
    oldDepartment.getProjects().remove(project);
    newDepartmentById.getProjects().add(project);

    Project projectUpdated = this.projectDao.update(project)
      .orElseThrow(InvalidRequestBodyException::new);
    this.cacheService.evictCachedProjects();
    return ProjectMapper.INSTANCE.projectToProjectResponseDto(projectUpdated);
  }

  @Override
  public void deleteProject(Long id) throws EntityNotFoundException {
    this.projectDao.deleteById(id);
    this.cacheService.evictCachedProjects();
  }

  @Override
  public List<ProjectAssignmentStatisticsDto> getProjectsWithHighestHoursSpentInDescendingOrder(int limit) {
    return this.projectDao.findWithHighestTotalHoursSpentOnInDescendingOrder(limit)
      .map(ProjectMapper.INSTANCE::tupleToProjectAssignmentStatisticsDto)
      .toList();
  }
}
