package org.hr.employee.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.JDBCException;
import org.hibernate.jpa.HibernateHints;
import org.hr.employee.entity.Department;
import org.hr.employee.entity.DepartmentLocation;

import java.util.Optional;

@ApplicationScoped
public class DepartmentDAOImpl implements  DepartmentDAO {

  private final EntityManager entityManager;

  @Inject
  public DepartmentDAOImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  @NonNull
  public Optional<Department> saveDepartment(Department department)
    throws jakarta.validation.ConstraintViolationException {

    this.entityManager.persist(department);
    this.entityManager.flush();
    return Optional.of(department);
  }

  @Override
  public Optional<Department> findDepartmentById(Long id) {
    return Optional.ofNullable(this.entityManager.find(Department.class, id));
  }

  @Override
  public Optional<Integer> deleteDepartment(Long id) {
    Query query = this.entityManager.createNamedQuery("deleteDepartmentById");
    query.setParameter("id", id)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT);
    return Optional.of(Integer.valueOf(query.executeUpdate()));
  }

  @Override
  public Optional<DepartmentLocation> findDepartmentLocationById(Long id) {
    return Optional.ofNullable(this.entityManager.find(DepartmentLocation.class, id));
  }

  @Override
  public Optional<DepartmentLocation> findDepartmentLocationByLocation(String location) throws NoResultException {
    TypedQuery<DepartmentLocation> query = this.entityManager.createNamedQuery(
      "findDepartmentLocationByLocation", DepartmentLocation.class
    );
    query.setParameter("location", location)
      .setHint(HibernateHints.HINT_READ_ONLY, true)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT);
    return Optional.of(query.getSingleResult());
  }


  @Override
  public Optional<DepartmentLocation> saveDepartmentLocation(DepartmentLocation departmentLocation)
    throws ConstraintViolationException, JDBCException {

    this.entityManager.persist(departmentLocation);
    this.entityManager.flush();
    return Optional.of(departmentLocation);
  }

  @Override
  public Optional<DepartmentLocation> updateDepartmentLocation(DepartmentLocation departmentLocation)
    throws ConstraintViolationException, JDBCException {

    DepartmentLocation updatedDepartmentLocation = this.entityManager.merge(departmentLocation);
    this.entityManager.flush();
    return Optional.of(updatedDepartmentLocation);
  }

  @Override
  public Optional<Integer> deleteDepartmentLocationById(Long id) {
    Query query = this.entityManager.createNamedQuery("deleteLocationById");
    query.setParameter("id", id)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT);
    return Optional.of(query.executeUpdate());
  }

  @Override
  public Optional<Department> updateDepartment(Department department) throws ConstraintViolationException, JDBCException {
    Department updatedDepartment = this.entityManager.merge(department);
    this.entityManager.flush();
    return Optional.of(updatedDepartment);
  }


  @Override
  public Optional<Department> findDepartmentByName(String name) throws NoResultException {
    TypedQuery<Department> query = this.entityManager.createNamedQuery("findDepartmentByName", Department.class);
    query.setParameter("name", name)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT);
    return Optional.of(query.getSingleResult());
  }
}
