package org.hr.employee.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.HibernateHints;
import org.hr.employee.entity.Employee;

import java.util.Optional;
import java.util.stream.Stream;

@ApplicationScoped
public class EmployeeDAOImpl implements EmployeeDAO {

  private final EntityManager entityManager;

  @Inject
  public EmployeeDAOImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Optional<Employee> findEmployeeById(String id) {
    Employee employee = this.entityManager.find(Employee.class, id);
    return Optional.ofNullable(employee);
  }

  @Override
  public Stream<Employee> findEmployeeByFirstname(String firstname) {
    TypedQuery<Employee> query = this.entityManager.createNamedQuery("findEmployeesByFirstname", Employee.class);
    query.setParameter("firstname", firstname)
      .setMaxResults(MAX_ROWS)
      .setHint(HibernateHints.HINT_READ_ONLY, true)
      .setHint(HibernateHints.HINT_CACHEABLE, true)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT);
    return query.getResultList().stream();
  }

  @Override
  public Optional<Employee> saveEmployee(Employee employee) throws ConstraintViolationException {
    this.entityManager.persist(employee);
    this.entityManager.flush();
    return Optional.of(employee);
  }

  @Override
  public Optional<Employee> updateEmployee(Employee employee) throws ConstraintViolationException {
    Employee updatedEmployee = this.entityManager.merge(employee);
    this.entityManager.flush();
    return Optional.of(updatedEmployee);
  }

  @Override
  public Optional<Integer> deleteEmployeeById(String employeeId) {
    Query query = this.entityManager.createNamedQuery("deleteEmployeeById");
    query.setParameter("id", employeeId)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT);
    return Optional.of(Integer.valueOf(query.executeUpdate()));
  }


}
