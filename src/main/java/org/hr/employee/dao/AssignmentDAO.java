package org.hr.employee.dao;

import jakarta.validation.ConstraintViolationException;
import org.hibernate.JDBCException;
import org.hr.employee.entity.Assignment;

import java.util.Optional;

public interface AssignmentDAO {
  Optional<Assignment> findAssignmentById(Long id);

  Optional<Assignment> saveAssignment(Assignment assignment)
    throws ConstraintViolationException, JDBCException;

  Optional<Assignment> updateAssignment(Assignment assignment);
  Optional<Integer> deleteAssignment(Long id);
}
