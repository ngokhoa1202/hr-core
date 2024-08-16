package org.hr.employee.dao;

import org.hr.employee.entity.Assignment;
import org.hr.employee.entity.Project;

import java.util.Optional;

public interface ProjectDAO {
  static final Integer MAX_ROWS = 20;
  static final Integer QUERY_TIMEOUT = 2000; // in ms

  Optional<Project> findProjectById(Long id);
  Optional<Project> saveProject(Project project);
  Optional<Project> updateProject(Project project);
  Optional<Integer> deleteProject(Long projectId);

}

