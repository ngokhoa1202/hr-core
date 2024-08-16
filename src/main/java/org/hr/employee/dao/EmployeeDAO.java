package org.hr.employee.dao;

import org.hr.employee.entity.Employee;

import java.util.Optional;
import java.util.stream.Stream;

public interface EmployeeDAO {
  static final Integer MAX_ROWS = 20;
  static final Integer QUERY_TIMEOUT = 2000; // in ms

  Optional<Employee> findEmployeeById(String id);

  Stream<Employee> findEmployeeByFirstname(String firstname);
  Optional<Employee> saveEmployee(Employee employee);
  Optional<Employee> updateEmployee(Employee employee);
  Optional<Integer> deleteEmployeeById(String employeeId);
}
