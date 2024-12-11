package org.hr.employee.dao;

import jakarta.enterprise.context.ApplicationScoped;
import org.hr.config.BaseDAO;
import org.hr.employee.entity.DepartmentLocation;

@ApplicationScoped
public class DepartmentLocationDao extends BaseDAO<DepartmentLocation> {

  public DepartmentLocationDao() {
    super(DepartmentLocation.class);
  }
}
