package org.hr.employee.service;

import org.hr.employee.dto.ProjectCreationDTO;
import org.hr.employee.dto.ProjectDTO;

public interface ProjectService {
  ProjectDTO getProject(Long id);
  ProjectDTO saveProject(ProjectCreationDTO projectCreationDTO);
  ProjectDTO updateProject(Long projectId, ProjectCreationDTO projectCreationDTO);
  void deleteProject(Long projectId);
}
