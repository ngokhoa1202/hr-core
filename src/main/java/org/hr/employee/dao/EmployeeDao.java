package org.hr.employee.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import org.hibernate.jpa.HibernateHints;
import org.hr.config.BaseDAO;
import org.hr.employee.dto.employee.EmployeeAssignmentStatisticsDto;
import org.hr.employee.entity.Employee;

import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
public class EmployeeDao extends BaseDAO<Employee> {

  public static final Integer QUERY_TIMEOUT = 2000;
  public static final Integer MAX_RESULTS_ALLOWED = 100;

  protected EmployeeDao() {
    super(Employee.class);
  }

  /**
   * Find employees within department whose employee id prefix matches given employee id.
   * Native query converted to Named Query is used
   * The fetched department is not null
   */
  public Stream<Employee> findByEmployeeIdPrefix(String employeeId, int startIndex, int limit) {
    TypedQuery<Employee> query = this.entityManager.createNamedQuery(
      Employee.EMPLOYEES_BY_EMPLOYEE_ID_PREFIX_QUERY, Employee.class
    );
    query.setParameter("employeeId", employeeId + "%")
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT)
      .setHint(HibernateHints.HINT_READ_ONLY, true)
      .setFirstResult(startIndex)
      .setMaxResults(limit);
    return query.getResultStream();
  }

  /**
   * Find all employees whose either firstname or lastname or middlename starts with name
   * Static named entity graph and named query is used
   */
  public Stream<Employee> findByFirstnamePrefixOrLastnamePrefixOrMiddlePrefix(String name, int startIndex, int limit) {
    TypedQuery<Employee> query = this.entityManager.createNamedQuery(Employee.EMPLOYEES_BY_NAME_PREFIX_QUERY, Employee.class);
    query.setHint(HibernateHints.HINT_READ_ONLY, true)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT)
      .setParameter("name", name + "%")
      .setFirstResult(startIndex)
      .setMaxResults(limit);
    return query.getResultStream();
  }

  public Stream<EmployeeAssignmentStatisticsDto> findAssignmentStatisticsById(UUID id) {
    TypedQuery<EmployeeAssignmentStatisticsDto> query = this.entityManager.createNamedQuery(
      Employee.EMPLOYEES_ASSIGNMENT_STATISTICS_BY_ID_QUERY, EmployeeAssignmentStatisticsDto.class
    );
    query.setHint(HibernateHints.HINT_READ_ONLY, true)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT)
      .setParameter("id", id);
    return query.getResultStream();
  }
}
