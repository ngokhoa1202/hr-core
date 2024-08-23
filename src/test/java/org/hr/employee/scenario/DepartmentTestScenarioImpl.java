package org.hr.employee.scenario;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Validation;
import org.hr.employee.dto.DepartmentCreationDTO;
import org.hr.employee.dto.DepartmentDTO;
import org.hr.employee.dto.DepartmentLocationCreationDTO;
import org.hr.employee.dto.DepartmentLocationDTO;
import org.hr.employee.entity.Department;

import java.sql.Date;
import java.util.Optional;

@ApplicationScoped
public class DepartmentTestScenarioImpl implements DepartmentTestScenario {

  public org.hibernate.exception.ConstraintViolationException mockHibernateUniqueViolationException(String constraintName) {
    return new org.hibernate.exception.ConstraintViolationException(
      "Something",
      new org.postgresql.util.PSQLException("Something", org.postgresql.util.PSQLState.UNIQUE_VIOLATION),
      constraintName
    );
  }

  public DepartmentDTO mockDepartmentDTO() {
    return new DepartmentDTO(Long.valueOf(6L), "IT", Date.valueOf("2020-01-01"));
  }

  public String mockInvalidJwtToken() {
    return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJwb2xpdGljcyIsIm5hbWUiOiJjb21tdW5pc3QiLCJpYXQiOjE1MTYyMzkwMjJ9.mjiaH4BE7TQ8rtpSzYPEcG9SxtuBQMAbVwGPg4zGyz4";
  }

  public DepartmentCreationDTO mockDepartmentCreationDTO() {
    return new DepartmentCreationDTO("IT", Date.valueOf("2020-01-01"));
  }

  public DepartmentCreationDTO mockDepartmentCreationDTOWithInvalidName() {
    return new DepartmentCreationDTO("IT2321", Date.valueOf("2020-01-01"));
  }

  public DepartmentLocationCreationDTO mockDepartmentLocationCreationDTOWithInvalidLocation() {
    return new DepartmentLocationCreationDTO("", 6L);
  }

  public Long mockDepartmentId() {
    return 6L;
  }

  public Long mockDepartmentLocationId() {
    return 7L;
  }

  public DepartmentLocationCreationDTO mockDepartmentLocationCreationDTO() {
    return new DepartmentLocationCreationDTO("Tokyo", 6L);
  }

  public DepartmentLocationDTO mockDepartmentLocationDTO() {
    return new DepartmentLocationDTO(7L, "Tokyo", 6L);
  }

  public jakarta.validation.ConstraintViolationException mockJakartaConstraintViolationException(Object o) {
    return new jakarta.validation.ConstraintViolationException(
      "Some violation",
      Validation.buildDefaultValidatorFactory()
        .getValidator()
        .validate(o)
    );
  }

  @Override
  public Optional<Department> mockOptionalDepartment() {
    return Optional.of(
      Department.builder()
        .id(6L)
        .name("IT")
        .startDate(Date.valueOf("2020-01-01"))
        .build()
    );
  }
}
