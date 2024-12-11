package org.hr.employee.service;

import jakarta.validation.Valid;
import org.hr.employee.dto.project.ProjectAssignmentStatisticsDto;
import org.hr.employee.dto.project.ProjectPayloadDto;
import org.hr.employee.dto.project.ProjectResponseDto;
import org.hr.employee.entity.Project;
import org.hr.exception.InvalidRequestBodyException;

import java.util.List;

public interface ProjectService {
  ProjectResponseDto getProject(Long id);
  ProjectResponseDto createProject(@Valid ProjectPayloadDto projectCreationDTO);
  ProjectResponseDto updateProject(Long projectId, @Valid ProjectPayloadDto projectCreationDTO);

  List<ProjectAssignmentStatisticsDto> getProjectsWithHighestHoursSpentInDescendingOrder(int limit);

  void deleteProject(Long projectId);

  static void ensureProjectIntegrity(Project persistentProject, Project transientProject) {
    if ( ! persistentProject.getArea().equals(transientProject.getArea()) ) {
      throw new InvalidRequestBodyException("area", Project.class.getSimpleName());
    }
    if ( ! persistentProject.getName().equals(transientProject.getName()) ) {
      throw new InvalidRequestBodyException("name", Project.class.getSimpleName());
    }
  }
}
