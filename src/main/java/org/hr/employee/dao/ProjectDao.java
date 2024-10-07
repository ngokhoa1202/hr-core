package org.hr.employee.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.hibernate.jpa.HibernateHints;
import org.hr.config.BaseDAO;
import org.hr.employee.entity.Project;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@ApplicationScoped
public class ProjectDao extends BaseDAO<Project> {

  public ProjectDao() {
    super(Project.class);
  }

  /**
   * Find projects that has the highest total hours spent => Calculate the sum of hour per assignment. Then compare
   * Criteria query is employed
   * The size of results is limited in dao layer
   */
  public Stream<Tuple> findWithHighestTotalHoursSpentOnInDescendingOrder(int limit) {
    CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> criteriaQuery = builder.createTupleQuery();
    Root<Project> root = criteriaQuery.from(Project.class);
    root.joinSet("assignments", JoinType.INNER);
    root.join("managedDepartment", JoinType.INNER);

    Expression<Long> projectId = root.get("id");
    Expression<Long> departmentId = root.get("managedDepartment").get("id");
    Expression<String> departmentName = root.get("managedDepartment").get("name");
    Expression<LocalDateTime> departmentStartDate = root.get("managedDepartment").get("startDate");
    Expression<String> projectName = root.get("name");
    Expression<String> projectArea = root.get("area");
    Expression<Double> hoursSpentPerAssignment = builder.avg(
      root.get("assignments").get("numberOfHours")
    );
    Expression<Long> totalHours = builder.sum(root.get("assignments").get("numberOfHours")).as(Long.class);
    Expression<Long> numberOfAssignments = builder.count(root.get("assignments").get("id"));

    criteriaQuery.multiselect(
      projectId.alias("projectId"),
      departmentId.alias("departmentId"),
      departmentName.alias("departmentName"),
      departmentStartDate.alias("departmentStartDate"),
      projectName.alias("projectName"),
      projectArea.alias("projectArea"),
      hoursSpentPerAssignment.alias("hoursSpentPerAssignment"),
      totalHours.alias("totalHours"),
      numberOfAssignments.alias("numberOfAssignments")
    )
      .groupBy(projectId, departmentId, departmentName, departmentStartDate)
      .orderBy(builder.desc(totalHours));

    TypedQuery<Tuple> query = this.entityManager.createQuery(criteriaQuery)
      .setHint(HibernateHints.HINT_READ_ONLY, true)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT * 2)
      .setMaxResults(limit);
    return query.getResultStream();
  }


}
