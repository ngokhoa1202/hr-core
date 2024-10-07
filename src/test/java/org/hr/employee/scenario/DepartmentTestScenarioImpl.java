package org.hr.employee.scenario;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Validation;
import org.hr.employee.dto.department.DepartmentPayloadDto;
import org.hr.employee.dto.department.DepartmentResponseDto;
import org.hr.employee.dto.department.location.DepartmentLocationPayloadDto;
import org.hr.employee.dto.department.location.DepartmentLocationResponseDTO;
import org.hr.employee.entity.Department;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class DepartmentTestScenarioImpl implements DepartmentTestScenario {

  final Long DEPARTMENT_ID = 47L;
  final String DEPARTMENT_NAME = "Marketing";
  final LocalDateTime DEPARTMENT_START_DATE = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

  public org.hibernate.exception.ConstraintViolationException mockHibernateUniqueViolationException(String constraintName) {
    return new org.hibernate.exception.ConstraintViolationException(
      "Something",
      new org.postgresql.util.PSQLException("Something", org.postgresql.util.PSQLState.UNIQUE_VIOLATION),
      constraintName
    );
  }

  public DepartmentResponseDto mockDepartmentResponseDto() {
    return new DepartmentResponseDto(
      DEPARTMENT_ID,
      DEPARTMENT_NAME,
      DEPARTMENT_START_DATE
    );
  }

  public String mockInvalidJwtToken() {
    return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJwb2xpdGljcyIsIm5hbWUiOiJjb21tdW5pc3QiLCJpYXQiOjE1MTYyMzkwMjJ9.mjiaH4BE7TQ8rtpSzYPEcG9SxtuBQMAbVwGPg4zGyz4";
  }

  public DepartmentPayloadDto mockDepartmentCreationDTO() {
    return new DepartmentPayloadDto("IT", Date.valueOf("2020-01-01"));
  }

  public DepartmentPayloadDto mockDepartmentCreationDTOWithInvalidName() {
    return new DepartmentPayloadDto("IT2321", Date.valueOf("2020-01-01"));
  }

  public DepartmentLocationPayloadDto mockDepartmentLocationCreationDTOWithInvalidLocation() {
    return new DepartmentLocationPayloadDto("", 6L);
  }

  public Long mockDepartmentId() {
    return 6L;
  }

  public Long mockDepartmentLocationId() {
    return 7L;
  }

  public DepartmentLocationPayloadDto mockDepartmentLocationCreationDTO() {
    return new DepartmentLocationPayloadDto("Tokyo", 6L);
  }

  public DepartmentLocationResponseDTO mockDepartmentLocationDTO() {
    return new DepartmentLocationResponseDTO(7L, "Tokyo", 6L);
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
