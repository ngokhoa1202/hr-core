package org.hr.employee.dao;

import org.hr.employee.entity.Department;
import org.hr.employee.entity.DepartmentLocation;

import java.util.Optional;

public interface DepartmentDAO {
  static final Integer QUERY_TIMEOUT = 1000;

  Optional<Department> findDepartmentById(Long id);
  Optional<Department> findDepartmentByName(String name);
  Optional<Department> saveDepartment(Department department);
  Optional<Department> updateDepartment(Department department);
  Optional<Integer> deleteDepartment(Long id);

  Optional<DepartmentLocation> findDepartmentLocationById(Long id);
  Optional<DepartmentLocation> findDepartmentLocationByLocation(String location);
  Optional<DepartmentLocation> updateDepartmentLocation(DepartmentLocation departmentLocation);
  Optional<DepartmentLocation> saveDepartmentLocation(DepartmentLocation departmentLocation);

  Optional<Integer> deleteDepartmentLocationById(Long id);

}
