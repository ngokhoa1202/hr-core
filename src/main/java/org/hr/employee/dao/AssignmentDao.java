package org.hr.employee.dao;

import jakarta.enterprise.context.ApplicationScoped;
import org.hr.config.BaseDAO;
import org.hr.employee.entity.Assignment;

@ApplicationScoped
public class AssignmentDao extends BaseDAO<Assignment> {

  public static final Integer QUERY_TIMEOUT = 1000;

  public AssignmentDao() {
    super(Assignment.class);
  }
}
