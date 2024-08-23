package org.hr.employee.service;

import groovy.transform.ASTTest;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.hr.employee.dao.DepartmentDAO;
import org.hr.employee.dto.DepartmentDTO;
import org.hr.employee.entity.Department;
import org.hr.employee.scenario.DepartmentTestScenario;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.mapper.HumanResourceException;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

import java.util.Optional;

@QuarkusTest
public class DepartmentServiceTest {

  @InjectMock
  private DepartmentDAO departmentDAO;

  @Inject
  private DepartmentService departmentService;

  @Inject
  private DepartmentTestScenario scenario;

  @Test
  @Order(1)
  public void GivenDepartmentId_WhenGettingDepartment_ReturnDepartmentDTO() {
    Long id = this.scenario.mockDepartmentId();
    final Optional<Department> expectedDepartmentOptional = this.scenario.mockOptionalDepartment();

    Mockito.when(this.departmentDAO.findDepartmentById(id))
      .thenReturn(expectedDepartmentOptional);

    DepartmentDTO expectedDepartmentDTO = this.scenario.mockDepartmentDTO();
    DepartmentDTO departmentDTO = this.departmentService.getDepartment(id);

    assertEquals(expectedDepartmentDTO, departmentDTO);
  }

  @Test
  @Order(2)
  public void GivenDepartmentIdNotExisted_WhenGettingDepartment_ThrowEntityNotFoundException() {
    Long id = this.scenario.mockDepartmentId();
    Mockito.when(this.departmentDAO.findDepartmentById(id))
      .thenReturn(Optional.empty());

    HumanResourceException ex = assertThrows(
      EntityNotFoundException.class,
      () -> {
        this.departmentService.getDepartment(id);
      }
    );
    assertInstanceOf(EntityNotFoundException.class, ex);
  }
}
