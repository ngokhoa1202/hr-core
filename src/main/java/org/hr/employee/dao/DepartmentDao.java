package org.hr.employee.dao;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.jpa.HibernateHints;
import org.hr.config.BaseDAO;
import org.hr.employee.dto.department.DepartmentEmployeeStatisticsDto;
import org.hr.employee.entity.Department;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.Optional;


@ApplicationScoped
public class DepartmentDao extends BaseDAO<Department> {

  public static final int DEFAULT_MAX_RESULTS = 100;

  protected DepartmentDao() {
    super(Department.class);
  }

  public Stream<DepartmentEmployeeStatisticsDto> findEmployeeStatisticsById(Long id) {

    TypedQuery<DepartmentEmployeeStatisticsDto> query = this.entityManager.createNamedQuery(
      Department.DEPARTMENT_EMPLOYEE_STATISTICS_BY_ID_QUERY,
      DepartmentEmployeeStatisticsDto.class
    );
    query.setParameter("id", id)
      .setHint(HibernateHints.HINT_READ_ONLY, true)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT);
    return query.getResultStream();
  }

  /**
   * Find employees whose salary is greater or equal to a given salary
   * Built-in entityManager API with named entity graph is used to override default lazy loading mechanism
   */
  public Optional<Department> findWithEmployeesById(Long id) {
    EntityGraph<?> entityGraph = this.entityManager.createEntityGraph(Department.DEPARTMENT_EMPLOYEE_GRAPH);
    HashMap<String, Object> properties = new HashMap<String, Object>();
    properties.put("jakarta.persistence.fetchgraph", entityGraph);
    Department department = this.entityManager.find(Department.class, id, properties);
    return Optional.ofNullable(department);
  }

  /**
   * Find employees whose hours spent on each assignment is lowest
   * The predicate is implemented on the database layer, so the service should do nothing except convert raw to dto data
   * Named query with Tuple in Jakarta persistence is used
   * Any employee who has not joined any project yet will not be queried because inner join is used
   */
  public Stream<Tuple> findEmployeesWithLowestHoursSpentPerAssignmentInDescendingOrderById(Long id, int limit) {

    TypedQuery<Tuple> query = this.entityManager.createNamedQuery(
      Department.DEPARTMENT_EMPLOYEES_WITH_LOWEST_HOURS_SPENT_PER_ASSIGNMENTS_IN_DESCENDING_ORDER_BY_ID,
      Tuple.class
    );
    query.setParameter("id", id)
      .setParameter("limit", limit)
      .setHint(HibernateHints.HINT_READ_ONLY, true)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT * 3);
    return query.getResultStream();
  }

  public Stream<Tuple> findEmployeesWithLowestHoursSpentPerAssignmentInAscendingOrderById(Long id, int limit) {
    TypedQuery<Tuple> query = this.entityManager.createNamedQuery(
      Department.DEPARTMENT_EMPLOYEES_WITH_LOWEST_HOURS_SPENT_PER_ASSIGNMENTS_IN_ASCENDING_ORDER_BY_ID,
      Tuple.class
    );
    query.setParameter("id", id)
      .setParameter("limit", limit)
      .setHint(HibernateHints.HINT_READ_ONLY, true)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT * 3);
    return query.getResultStream();
  }

  @Override
  public Optional<Department> findById(Long id) {
    EntityGraph<?> entityGraph = this.entityManager.createEntityGraph(Department.DEPARTMENT_LOCATIONS_GRAPH);
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put("jakarta.persistence.fetchgraph", entityGraph);
    Department department = this.entityManager.find(Department.class, id, properties);
    return Optional.ofNullable(department);
  }

  @Override
  public Stream<Department> findAll(int startIndex, int limit) {
    CriteriaQuery<Department> criteriaQuery = this.entityManager.getCriteriaBuilder()
      .createQuery(Department.class);
    Root<Department> root = criteriaQuery.from(Department.class);
    criteriaQuery.select(root).distinct(true);

    EntityGraph<?> entityGraph = this.entityManager.createEntityGraph(Department.DEPARTMENT_LOCATIONS_GRAPH);

    return this.entityManager.createQuery(criteriaQuery)
      .setFirstResult(startIndex)
      .setMaxResults(limit)
      .setHint(HibernateHints.HINT_READ_ONLY, true)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT)
      .setHint("jakarta.persistence.fetchgraph", entityGraph)
      .getResultStream();
  }

  @Override
  public Stream<Department> findAll() {
    return this.findAll(0, DEFAULT_MAX_RESULTS);
  }
}
