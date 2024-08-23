package org.hr.employee.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import org.hibernate.JDBCException;
import org.hibernate.jpa.HibernateHints;
import org.hr.employee.entity.Assignment;

import java.util.Optional;

@ApplicationScoped
public class AssignmentDAOImpl implements AssignmentDAO {

  public static final Integer QUERY_TIMEOUT = 1000;

  private final EntityManager entityManager;

  @Inject
  public AssignmentDAOImpl(@NonNull EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Optional<Assignment> findAssignmentById(Long id) {
    return Optional.ofNullable(this.entityManager.find(Assignment.class, id));
  }

  @Override
  public Optional<Assignment> saveAssignment(Assignment assignment) throws ConstraintViolationException, JDBCException {
    this.entityManager.persist(assignment);
    this.entityManager.flush();
    return Optional.of(assignment);
  }

  @Override
  public Optional<Assignment> updateAssignment(Assignment assignment) {
    Assignment updatedAssignment = this.entityManager.merge(assignment);
    this.entityManager.flush();
    return Optional.of(updatedAssignment);
  }

  @Override
  public Optional<Integer> deleteAssignment(Long id) {
    return Optional.of(
      this.entityManager.createNamedQuery("deleteAssignmentById")
      .setParameter("id", id)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT)
      .executeUpdate()
    );
  }
}
