package org.hr.employee.resource;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.hibernate.JDBCException;
import org.hr.employee.dto.employee.EmployeeAssignmentStatisticsDto;
import org.hr.employee.dto.employee.EmployeePayloadDto;
import org.hr.employee.dto.employee.EmployeeResponseDto;
import org.hr.employee.dto.TotalNumberDTO;
import org.hr.employee.service.EmployeeService;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.converter.ExceptionConverter;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("employees")
@Consumes(MediaType.APPLICATION_JSON)
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@RequiredArgsConstructor
@Authenticated
public class EmployeeResource {

  private final EmployeeService employeeService;
  private final ExceptionConverter exceptionConverter;
  private final String path = "api/employees/";

  @Path("{id}")
  @GET
  @RolesAllowed({"admin", "user"})
  public RestResponse<EmployeeResponseDto> getEmployee(@RestPath(value="id") UUID id)
    throws EntityNotFoundException.HumanResourceException {

    EmployeeResponseDto employeeDTO = this.employeeService.getEmployee(id);
    return RestResponse.ok(employeeDTO);
  }

  @Path("pages/{startIndex}/limit/{limit}")
  @GET
  @RolesAllowed({"admin"})
  public RestResponse<List<EmployeeResponseDto>> getEmployees(
    @RestPath("startIndex") int startIndex, @RestPath("limit") int limit) {
    List<EmployeeResponseDto> employeeDTOs = this.employeeService.getEmployees(startIndex, limit);
    return RestResponse.ok(employeeDTOs);
  }

  @POST
  @RolesAllowed({"admin"})
  public RestResponse<String> createEmployee(
    @RequestBody EmployeePayloadDto employeePayloadDTO
  ) throws EntityNotFoundException.HumanResourceException {

    try {
      EmployeeResponseDto employeeDTO = this.employeeService.createEmployee(employeePayloadDTO);
      return RestResponse.created(URI.create(this.path + employeeDTO.id().toString()));
    } catch (JDBCException | ConstraintViolationException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("{id}")
  @PUT
  @RolesAllowed({"admin"})
  public RestResponse<String> updateEmployee(
    @RestPath(value = "id") UUID id, @RequestBody EmployeePayloadDto employeeWithoutIdDTO
  ) throws EntityNotFoundException.HumanResourceException {

    try {
      EmployeeResponseDto employee = this.employeeService.updateEmployee(id, employeeWithoutIdDTO);
      return RestResponse.created(URI.create(employee.id().toString()));
    } catch (JDBCException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("{id}")
  @DELETE
  @RolesAllowed({"admin"})
  public RestResponse<String> deleteEmployee(@RestPath(value = "id") UUID id) throws EntityNotFoundException.HumanResourceException {
    this.employeeService.deleteEmployee(id);
    return RestResponse.noContent();
  }

  @Path("total")
  @GET
  @RolesAllowed({"admin"})
  public RestResponse<TotalNumberDTO> getTotalNumberOfEmployees() {
    return RestResponse.ok(this.employeeService.getTotalNumberOfEmployees());
  }

  @Path("pages/{startIndex}/limit/{limit}/employee-ids/{employeeId}")
  @GET
  @RolesAllowed({"admin"})
  public RestResponse<List<EmployeeResponseDto>> getEmployeesByEmployeeId(
    @NotBlank @RestPath("employeeId") String employeeId, @RestPath("startIndex") int startIndex,
    @RestPath("limit") int limit
  ) throws EntityNotFoundException.HumanResourceException {

    List<EmployeeResponseDto> employeeResponseDtos = this.employeeService.getEmployeesByEmployeeId(employeeId, startIndex, limit);
    return RestResponse.ok(employeeResponseDtos);
  }

  @Path("pages/{startIndex}/limit/{limit}/names/{name}")
  @GET
  @RolesAllowed({"admin"})
  public RestResponse<List<EmployeeResponseDto>> getEmployeesByName(
    @RestPath("name") String name, @RestPath("startIndex") int startIndex, @RestPath("limit") int limit
  ) throws EntityNotFoundException.HumanResourceException {

    List<EmployeeResponseDto> employeeResponseDtos = this.employeeService.getEmployeesByName(name, startIndex, limit);
    return RestResponse.ok(employeeResponseDtos);
  }

  @Path("{id}/assignments/statistics")
  @GET
  @RolesAllowed({"admin"})
  public RestResponse<EmployeeAssignmentStatisticsDto> getEmployeeWithAssignmentStatistics(@RestPath("id") UUID id)
    throws EntityNotFoundException.HumanResourceException {

    EmployeeAssignmentStatisticsDto employeeAssignmentStatisticsDto = this.employeeService.getEmployeeWithAssignmentStatistics(id);
    return RestResponse.ok(employeeAssignmentStatisticsDto);
  }
}
