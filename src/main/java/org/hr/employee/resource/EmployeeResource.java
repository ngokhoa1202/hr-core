package org.hr.employee.resource;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.hibernate.JDBCException;
import org.hr.employee.dto.EmployeeCreationDTO;
import org.hr.employee.dto.EmployeeDTO;
import org.hr.employee.service.EmployeeService;
import org.hr.exception.handler.ExceptionConverter;
import org.hr.exception.mapper.HumanResourceException;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;

@Path("employees")
@Consumes(MediaType.APPLICATION_JSON)
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Authenticated
public class EmployeeResource {

  private final EmployeeService employeeService;
  private final ExceptionConverter exceptionConverter;

  @Inject
  public EmployeeResource(EmployeeService employeeService, ExceptionConverter exceptionConverter) {
    this.employeeService = employeeService;
    this.exceptionConverter = exceptionConverter;
  }

  @Path("{id}")
  @GET
  @PermitAll
  public RestResponse<EmployeeDTO> getEmployee(@RestPath(value="id") String id)
    throws HumanResourceException {

    EmployeeDTO employeeDTO = this.employeeService.getEmployee(id);
    return RestResponse.ok(employeeDTO);
  }

  @POST
  @RolesAllowed({"admin", "user"})
  public RestResponse<String> createEmployee(
    @RequestBody EmployeeCreationDTO employeeCreationDTO
  ) throws HumanResourceException {

    try {
      EmployeeDTO employeeDTO = this.employeeService.saveEmployee(employeeCreationDTO);
      return RestResponse.created(URI.create(employeeDTO.id()));
    } catch (JDBCException | ConstraintViolationException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("{id}")
  @PUT
  @RolesAllowed({"admin", "user"})
  public RestResponse<String> updateEmployee(
    @RestPath(value = "id") String employeeId, @RequestBody EmployeeCreationDTO employeeWithoutIdDTO
  ) throws HumanResourceException {

    try {
      EmployeeDTO employee = this.employeeService.updateEmployee(employeeId, employeeWithoutIdDTO);
      return RestResponse.created(URI.create(employee.id()));
    } catch (JDBCException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("{id}")
  @DELETE
  public RestResponse<String> deleteEmployee(@RestPath(value = "id") String employeeId) throws HumanResourceException {
    this.employeeService.deleteEmployee(employeeId);
    return RestResponse.noContent();
  }
}
