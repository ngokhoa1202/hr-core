package org.hr.employee.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.hibernate.JDBCException;
import org.hr.employee.dto.*;
import org.hr.employee.dto.department.DepartmentEmployeeStatisticsDto;
import org.hr.employee.dto.department.DepartmentPayloadDto;
import org.hr.employee.dto.department.DepartmentResponseDto;
import org.hr.employee.dto.department.location.DepartmentLocationPayloadDto;
import org.hr.employee.dto.department.location.DepartmentLocationResponseDTO;
import org.hr.employee.dto.employee.EmployeeDepartmentAssignmentStatisticsDto;
import org.hr.employee.dto.employee.EmployeeResponseDto;
import org.hr.employee.service.DepartmentLocationService;
import org.hr.employee.service.DepartmentService;
import org.hr.exception.handler.ExceptionConverter;
import org.hr.exception.mapper.HumanResourceException;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;
import java.util.List;

@Path("departments")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@RequiredArgsConstructor
public class DepartmentResource {

  private final DepartmentService departmentService;
  private final DepartmentLocationService locationService;
  private final ExceptionConverter exceptionConverter;


  @Path("{id}")
  @GET
  @PermitAll
  public RestResponse<DepartmentResponseDto> getDepartment(@RestPath(value="id") Long id) throws HumanResourceException {
    DepartmentResponseDto departmentDTO = this.departmentService.getDepartment(id);
    return RestResponse.ok(departmentDTO);
  }


  @Path("")
  @POST
  @RolesAllowed({"admin"})
  public RestResponse<String> createDepartment(
    @RequestBody DepartmentPayloadDto departmentPayloadDTO
  ) throws HumanResourceException {

    try {
      DepartmentResponseDto departmentDTO = this.departmentService.createDepartment(departmentPayloadDTO);
      return RestResponse.created(URI.create(departmentDTO.id().toString()));
    } catch (ConstraintViolationException | JDBCException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("{id}")
  @PUT
  @RolesAllowed({"admin"})
  public RestResponse<DepartmentResponseDto> updateDepartment(
    @RestPath(value = "id") Long departmentId, @RequestBody DepartmentPayloadDto departmentPayloadDTO
  ) throws HumanResourceException  {

    try {
      DepartmentResponseDto departmentDTO = this.departmentService.updateDepartment(departmentId, departmentPayloadDTO);
      return RestResponse.created(URI.create(departmentDTO.id().toString()));
    } catch (ConstraintViolationException | JDBCException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("{id}")
  @DELETE
  @RolesAllowed({"admin"})
  public RestResponse<String> deleteDepartment(@RestPath(value="id") Long id) throws HumanResourceException {
    this.departmentService.deleteDepartment(id);
    return RestResponse.noContent();
  }

  @Path("locations/{id}")
  @GET
  @PermitAll
  public RestResponse<DepartmentLocationResponseDTO> getDepartmentLocation(@RestPath(value="id") Long id)
    throws HumanResourceException {

    DepartmentLocationResponseDTO locationDTO = this.locationService.getDepartmentLocation(id);
    return RestResponse.ok(locationDTO);
  }

  @Path("locations")
  @POST
  @RolesAllowed({"admin"})
  public RestResponse<String> createDepartmentLocation(
    @RequestBody DepartmentLocationPayloadDto departmentLocationPayloadDTO
  ) throws HumanResourceException {

    try {
      DepartmentLocationResponseDTO departmentLocationDTO = this.locationService.createDepartmentLocation(departmentLocationPayloadDTO);
      return RestResponse.created(URI.create(departmentLocationDTO.id().toString()));
    } catch (ConstraintViolationException | JDBCException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("locations/{id}")
  @PUT
  @RolesAllowed({"admin", "user"})
  public RestResponse<DepartmentLocationResponseDTO> updateDepartmentLocation(
    Long id, @RequestBody DepartmentLocationPayloadDto departmentLocationPayloadDTO
  ) throws HumanResourceException {

    try {
      DepartmentLocationResponseDTO locationDTO = this.locationService.updateDepartmentLocation(id, departmentLocationPayloadDTO);
      return RestResponse.created(URI.create(locationDTO.id().toString()));
    } catch (JDBCException | ConstraintViolationException ex) {
      throw this.exceptionConverter.convert(ex);
    }

  }

  @Path("locations/{id}")
  @DELETE
  @RolesAllowed({"admin"})
  public RestResponse<String> deleteDepartmentLocation(@RestPath(value="id") Long id)
    throws HumanResourceException {

    this.locationService.deleteDepartmentLocation(id);
    return RestResponse.noContent();
  }

  @Path("")
  @GET
  @RolesAllowed({"admin", "user"})
  public RestResponse<List<DepartmentResponseDto>> getDepartments() throws HumanResourceException {

    List<DepartmentResponseDto> departmentDTOs = this.departmentService.getDepartments();
    return RestResponse.ok(departmentDTOs);
  }

  @Path("total")
  @GET
  @RolesAllowed({"admin", "user"})
  public RestResponse<TotalNumberDTO> getTotalNumberOfDepartments() {

    TotalNumberDTO totalNumberDTO = this.departmentService.getTotalNumberOfDepartments();
    return RestResponse.ok(totalNumberDTO);
  }

  @Path("/{id}/employees/statistics")
  @GET
  @RolesAllowed({"admin"})
  public RestResponse<DepartmentEmployeeStatisticsDto> getDepartmentWithEmployeeStatistics(@RestPath(value = "id") Long id)
    throws HumanResourceException {

    DepartmentEmployeeStatisticsDto departmentEmployeeStatisticsDto = this.departmentService.getDepartmentWithEmployeeStatistics(id);
    return RestResponse.ok(departmentEmployeeStatisticsDto);
  }

  @Path("/{id}/employees/salaries/greater-or-equal/{salary}/limit/{limit}")
  @GET
  public RestResponse<List<EmployeeResponseDto>> getEmployeesWithSalaryGreaterOrEqualToGivenSalaryWithinDepartment(
    @RestPath("id") Long id, @RestPath("salary") int salary, @RestPath("limit") int limit
  ) throws HumanResourceException {

    List<EmployeeResponseDto> employeeResponseDtos = this.departmentService
      .getEmployeesWithSalaryGreaterOrEqualToGivenSalaryWithinDepartment(id, salary, limit);
    return RestResponse.ok(employeeResponseDtos);
  }

  @Path("/{id}/employees/assignments/statistics/limit/{limit}/desc")
  @GET
  public RestResponse<List<EmployeeDepartmentAssignmentStatisticsDto>> getEmployeesWithLowestHoursSpentPerAssignmentWithinDepartmentInDescendingOrder(
    @RestPath("id") Long id, @RestPath("limit") int limit
  ) throws HumanResourceException {

    List<EmployeeDepartmentAssignmentStatisticsDto> employeeDepartmentAssignmentStatisticsDtos =
      this.departmentService.getEmployeesWithLowestHoursSpentPerAssignmentWithinDepartmentInDescendingOrder(id, limit);
    return RestResponse.ok(employeeDepartmentAssignmentStatisticsDtos);
  }

  @Path("/{id}/employees/assignments/statistics/limit/{limit}/asc")
  @GET
  public RestResponse<List<EmployeeDepartmentAssignmentStatisticsDto>> getEmployeesWithLowestHoursSpentPerAssignmentWithinDepartmentInAscendingOrder(
    @RestPath("id") Long id, @RestPath("limit") int limit
  ) throws HumanResourceException {
    List<EmployeeDepartmentAssignmentStatisticsDto> employeeDepartmentAssignmentStatisticsDtos =
      this.departmentService.getEmployeesWithLowestHoursSpentPerAssignmentWithinDepartmentInAscendingOrder(id, limit);
    return RestResponse.ok(employeeDepartmentAssignmentStatisticsDtos);
  }


}{
    "williamboman/mason.nvim"
}
