package org.hr.employee.dao;

import org.hr.employee.entity.Assignment;

import java.util.Optional;

public interface AssignmentDAO {
  Optional<Assignment> findAssignmentById(Long id);
  Optional<Assignment> saveAssignment(Assignment assignment);
  Optional<Assignment> updateAssignment(Assignment assignment);
  Optional<Integer> deleteAssignment(Long id);
}
