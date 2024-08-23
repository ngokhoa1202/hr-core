package org.hr.employee.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.hibernate.JDBCException;
import org.hr.employee.dto.*;
import org.hr.employee.service.DepartmentService;
import org.hr.exception.*;
import org.hr.exception.handler.ExceptionConverter;
import org.hr.exception.mapper.HumanResourceException;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;

@Path("departments")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class DepartmentResource {

  private final DepartmentService departmentService;
  private final ExceptionConverter exceptionConverter;

  @Inject
  public DepartmentResource(
    DepartmentService departmentService,
    ExceptionConverter exceptionConverter) {

    this.departmentService = departmentService;
    this.exceptionConverter = exceptionConverter;
  }

  @Path("{id}")
  @GET
  @PermitAll
  public RestResponse<DepartmentDTO> getDepartment(@RestPath(value="id") Long id) throws EntityNotFoundException {
    DepartmentDTO departmentDTO = this.departmentService.getDepartment(id);
    return RestResponse.ok(departmentDTO);
  }

  @Path("")
  @POST
  @RolesAllowed({"admin"})
  public RestResponse<String> createDepartment(
    @RequestBody DepartmentCreationDTO departmentCreationDTO
  ) throws HumanResourceException {

    try {
      DepartmentDTO departmentDTO = this.departmentService.createDepartment(departmentCreationDTO);
      return RestResponse.created(URI.create(departmentDTO.id().toString()));
    } catch (ConstraintViolationException | JDBCException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("{id}")
  @PUT
  @RolesAllowed({"admin"})
  public RestResponse<DepartmentDTO> updateDepartment(
    @RestPath(value = "id") Long departmentId, @RequestBody DepartmentCreationDTO departmentCreationDTO
  ) throws HumanResourceException  {

    try {
      DepartmentDTO departmentDTO = this.departmentService.updateDepartment(departmentId, departmentCreationDTO);
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
  public RestResponse<DepartmentLocationDTO> getDepartmentLocation(@RestPath(value="id") Long id)
    throws HumanResourceException {

    DepartmentLocationDTO locationDTO = this.departmentService.getDepartmentLocation(id);
    return RestResponse.ok(locationDTO);
  }

  @Path("locations")
  @POST
  @RolesAllowed({"admin"})
  public RestResponse<String> createDepartmentLocation(
    @RequestBody DepartmentLocationCreationDTO locationCreationDTO
  ) throws HumanResourceException {
    try {
      DepartmentLocationDTO departmentLocationDTO = this.departmentService.createDepartmentLocation(locationCreationDTO);
      return RestResponse.created(URI.create(departmentLocationDTO.id().toString()));
    } catch (ConstraintViolationException | JDBCException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("locations/{id}")
  @PUT
  @RolesAllowed({"admin", "user"})
  public RestResponse<DepartmentLocationDTO> updateDepartmentLocation(
    Long id, @RequestBody DepartmentLocationCreationDTO locationCreationDTO
  ) throws HumanResourceException {

    try {
      DepartmentLocationDTO locationDTO = this.departmentService.updateDepartmentLocation(id, locationCreationDTO);
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

    this.departmentService.deleteDepartmentLocation(id);
    return RestResponse.noContent();
  }
}
