package org.hr.employee.resource;

import io.quarkus.logging.Log;
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
    Log.infof(
      "Get department by id"
    );
    Log.tracef(
      "%s layer: %s(%s:%s) captures %s request.",
      LoggingUtil.Layer.Resource.toString(),
      LoggingUtil.getCurrentMethod(),
      "id",
      Long.class.getName(),
      LoggingUtil.HTTP.Get.toString()
    );
    DepartmentResponseDto departmentDTO = this.departmentService.getDepartment(id);
    Log.infof(
      "Response Ok - Return a %s",
      DepartmentResponseDto.class.getName()
    );
    return RestResponse.ok(departmentDTO);
  }


  @Path("")
  @POST
  @RolesAllowed({"admin"})
  public RestResponse<String> createDepartment(
    @RequestBody DepartmentPayloadDto departmentPayloadDTO
  ) throws HumanResourceException {
    Log.infof("Create a new department");
    Log.tracef(
      "%s layer: %s(%s:%s) captures %s request.",
      LoggingUtil.Layer.Resource.toString(),
      LoggingUtil.getCurrentMethod(),
      "departmentPayloadDto",
      DepartmentPayloadDto.class.getName(),
      LoggingUtil.HTTP.Post.toString()
    );
    try {
      DepartmentResponseDto departmentDTO = this.departmentService.createDepartment(departmentPayloadDTO);
      Log.infof("Response Created - Successfully create department");
      return RestResponse.created(URI.create(departmentDTO.id().toString()));
    } catch (ConstraintViolationException | JDBCException ex) {
      HumanResourceException convertedExc = this.exceptionConverter.convert(ex);
      Log.infof(convertedExc, "An exception %s has been thrown", convertedExc.getClass().getName());
      throw convertedExc;
    }
  }

  @Path("{id}")
  @PUT
  @RolesAllowed({"admin"})
  public RestResponse<DepartmentResponseDto> updateDepartment(
    @RestPath(value = "id") Long departmentId, @RequestBody DepartmentPayloadDto departmentPayloadDTO
  ) throws HumanResourceException  {
    Log.infof("Update a department");
    Log.tracef(
      "%s layer: %s(%s:%s, %s:%s) captures %s request.",
      LoggingUtil.Layer.Resource.toString(),
      LoggingUtil.getCurrentMethod(),
      "id",
      Long.class.getName(),
      "departmentPayloadDTO",
      DepartmentPayloadDto.class.getName(),
      LoggingUtil.HTTP.Put.toString()
    );
    try {
      DepartmentResponseDto departmentDTO = this.departmentService.updateDepartment(departmentId, departmentPayloadDTO);
      Log.infof("Response Created - Successfully update department");
      return RestResponse.created(URI.create(departmentDTO.id().toString()));
    } catch (ConstraintViolationException | JDBCException ex) {
      HumanResourceException convertedExc = this.exceptionConverter.convert(ex);
      Log.infof(convertedExc, "An exception %s has been thrown", convertedExc.getClass().getName());
      throw convertedExc;
    }
  }

  @Path("{id}")
  @DELETE
  @RolesAllowed({"admin"})
  public RestResponse<String> deleteDepartment(@RestPath(value="id") Long id) throws HumanResourceException {
    Log.infof("Delete a department");
    Log.tracef(
      "%s layer: %s(%s:%s) captures %s request.",
      LoggingUtil.Layer.Resource.toString(),
      LoggingUtil.getCurrentMethod(),
      "id",
      Long.class.getName(),
      LoggingUtil.HTTP.Delete.toString()
    );
    this.departmentService.deleteDepartment(id);
    Log.infof("Response No content: Successfully delete department");
    return RestResponse.noContent();
  }

  @Path("locations/{id}")
  @GET
  @PermitAll
  public RestResponse<DepartmentLocationResponseDTO> getDepartmentLocation(@RestPath(value="id") Long id)
    throws HumanResourceException {

    Log.infof("Get a department location");
    Log.tracef(
      "%s layer: %s(%s:%s) captures %s request.",
      LoggingUtil.Layer.Resource.toString(),
      LoggingUtil.getCurrentMethod(),
      "id",
      Long.class.getName(),
      LoggingUtil.HTTP.Get.toString()
    );
    DepartmentLocationResponseDTO locationDTO = this.locationService.getDepartmentLocation(id);
    Log.infof(
      "Response Ok - Return a %s",
      DepartmentLocationResponseDTO.class.getName()
    );
    return RestResponse.ok(locationDTO);
  }

  @Path("locations")
  @POST
  @RolesAllowed({"admin"})
  public RestResponse<String> createDepartmentLocation(
    @RequestBody DepartmentLocationPayloadDto departmentLocationPayloadDTO
  ) throws HumanResourceException {

    Log.infof("Create a department location");
    Log.tracef(
      "%s layer: %s(%s:%s) captures %s request.",
      LoggingUtil.Layer.Resource.toString(),
      LoggingUtil.getCurrentMethod(),
      "departmentLocationPayloadDTO",
      DepartmentLocationPayloadDto.class.getName(),
      LoggingUtil.HTTP.Post.toString()
    );
    try {
      DepartmentLocationResponseDTO departmentLocationDTO = this.locationService.createDepartmentLocation(departmentLocationPayloadDTO);
      Log.infof("Response Created: Successfully create a department location");
      return RestResponse.created(URI.create(departmentLocationDTO.id().toString()));
    } catch (ConstraintViolationException | JDBCException ex) {
      HumanResourceException convertedExc = this.exceptionConverter.convert(ex);
      Log.infof(convertedExc, "An exception %s has been thrown", convertedExc.getClass().getName());
      throw convertedExc;
    }
  }

  @Path("locations/{id}")
  @PUT
  @RolesAllowed({"admin", "user"})
  public RestResponse<DepartmentLocationResponseDTO> updateDepartmentLocation(
    Long id, @RequestBody DepartmentLocationPayloadDto departmentLocationPayloadDTO
  ) throws HumanResourceException {

    Log.infof("Update a department location");
    Log.tracef(
      "%s layer: %s(%s:%s, %s:%s) captures %s request.",
      LoggingUtil.Layer.Resource.toString(),
      LoggingUtil.getCurrentMethod(),
      "id",
      Long.class.getName(),
      "departmentLocationPayloadDto",
      DepartmentLocationPayloadDto.class.getName(),
      LoggingUtil.HTTP.Put.toString()
    );
    try {
      DepartmentLocationResponseDTO locationDTO = this.locationService.updateDepartmentLocation(id, departmentLocationPayloadDTO);
      Log.infof("Response Created: Successfully update a department location");
      return RestResponse.created(URI.create(locationDTO.id().toString()));
    } catch (JDBCException | ConstraintViolationException ex) {
      HumanResourceException convertedExc = this.exceptionConverter.convert(ex);
      Log.infof(convertedExc, "An exception %s has been thrown", convertedExc.getClass().getName());
      throw convertedExc;
    }

  }

  @Path("locations/{id}")
  @DELETE
  @RolesAllowed({"admin"})
  public RestResponse<String> deleteDepartmentLocation(@RestPath(value="id") Long id)
    throws HumanResourceException {
    Log.infof("Delete a department location");
    Log.tracef(
      "%s layer: %s(%s:%s) captures %s request.",
      LoggingUtil.Layer.Resource.toString(),
      Long.class.getName(),
      LoggingUtil.getCurrentMethod(),
      "id",
      LoggingUtil.HTTP.Delete.toString()
    );
    this.locationService.deleteDepartmentLocation(id);
    return RestResponse.noContent();
  }

  @Path("")
  @GET
  @RolesAllowed({"admin", "user"})
  public RestResponse<List<DepartmentResponseDto>> getDepartments() throws HumanResourceException {
    Log.infof("Get all departments");
    Log.tracef(
      "%s layer: %s() captures %s request.",
      LoggingUtil.Layer.Resource.toString(),
      LoggingUtil.getCurrentMethod(),
      LoggingUtil.HTTP.Get.toString()
    );
    List<DepartmentResponseDto> departmentDTOs = this.departmentService.getDepartments();
    Log.infof("Response Ok: Return a %s", DepartmentResponseDto.class.getName());
    return RestResponse.ok(departmentDTOs);
  }

  @Path("total")
  @GET
  @RolesAllowed({"admin", "user"})
  public RestResponse<TotalNumberDTO> getTotalNumberOfDepartments() {
    Log.infof("Get the number of departments");
    Log.tracef(
      "%s layer: %s() captures %s request.",
      LoggingUtil.Layer.Resource.toString(),
      LoggingUtil.getCurrentMethod(),
      LoggingUtil.HTTP.Get.toString()
    );
    TotalNumberDTO totalNumberDTO = this.departmentService.getTotalNumberOfDepartments();
    Log.infof("Response Ok: Return a %s", TotalNumberDTO.class.getName());
    return RestResponse.ok(totalNumberDTO);
  }

  @Path("/{id}/employees/statistics")
  @GET
  @RolesAllowed({"admin"})
  public RestResponse<DepartmentEmployeeStatisticsDto> getDepartmentWithEmployeeStatistics(@RestPath(value = "id") Long id)
    throws HumanResourceException {

    Log.infof("Get department with its employee statistics");
    Log.tracef(
      "%s layer: %s(%s:%s) captures %s request.",
      LoggingUtil.Layer.Resource.toString(),
      LoggingUtil.getCurrentMethod(),
      "id",
      Long.class.getName(),
      LoggingUtil.HTTP.Get.toString()
    );
    DepartmentEmployeeStatisticsDto departmentEmployeeStatisticsDto = this.departmentService.getDepartmentWithEmployeeStatistics(id);
    Log.infof("Response Ok: Return a %s", DepartmentEmployeeStatisticsDto.class.getName());
    return RestResponse.ok(departmentEmployeeStatisticsDto);
  }

  @Path("/{id}/employees/salaries/greater-or-equal/{salary}/limit/{limit}")
  @GET
  public RestResponse<List<EmployeeResponseDto>> getEmployeesWithSalaryGreaterOrEqualToGivenSalaryWithinDepartment(
    @RestPath("id") Long id, @RestPath("salary") int salary, @RestPath("limit") int limit
  ) throws HumanResourceException {
    Log.infof("Get employees whose salary is greater or equal to given salary with a department");
    Log.tracef(
      "%s layer: %s(%s:%s, %s:%s, %s:%s) captures %s request.",
      LoggingUtil.Layer.Resource.toString(),
      LoggingUtil.getCurrentMethod(),
      "id",
      Long.class.getName(),
      "salary",
      int.class.getName(),
      "limit",
      int.class.getName(),
      LoggingUtil.HTTP.Get.toString()
    );
    List<EmployeeResponseDto> employeeResponseDtos = this.departmentService
      .getEmployeesWithSalaryGreaterOrEqualToGivenSalaryWithinDepartment(id, salary, limit);
    Log.infof("Response Ok: Return a %s", EmployeeResponseDto.class.getName());
    return RestResponse.ok(employeeResponseDtos);
  }

  @Path("/{id}/employees/assignments/statistics/limit/{limit}/desc")
  @GET
  public RestResponse<List<EmployeeDepartmentAssignmentStatisticsDto>> getEmployeesWithLowestHoursSpentPerAssignmentWithinDepartmentInDescendingOrder(
    @RestPath("id") Long id, @RestPath("limit") int limit
  ) throws HumanResourceException {

    Log.infof("Get employees whose hours spent per assignment is lowest within a department in descending order");
    Log.tracef(
      "%s layer: %s(%s:%s, %s:%s) captures %s request.",
      LoggingUtil.Layer.Resource.toString(),
      LoggingUtil.getCurrentMethod(),
      "id",
      int.class.getName(),
      "limit",
      int.class.getName(),
      LoggingUtil.HTTP.Get.toString()
    );
    List<EmployeeDepartmentAssignmentStatisticsDto> employeeDepartmentAssignmentStatisticsDtos =
      this.departmentService.getEmployeesWithLowestHoursSpentPerAssignmentWithinDepartmentInDescendingOrder(id, limit);
    Log.infof("Response Ok - Return a %s", EmployeeDepartmentAssignmentStatisticsDto.class.getName());
    return RestResponse.ok(employeeDepartmentAssignmentStatisticsDtos);
  }

  @Path("/{id}/employees/assignments/statistics/limit/{limit}/asc")
  @GET
  public RestResponse<List<EmployeeDepartmentAssignmentStatisticsDto>> getEmployeesWithLowestHoursSpentPerAssignmentWithinDepartmentInAscendingOrder(
    @RestPath("id") Long id, @RestPath("limit") int limit
  ) throws HumanResourceException {

    Log.infof("Get employees whose hours spent per assignment is lowest within a department in ascending order");
    Log.tracef(
      "%s layer: %s(%s:%s, %s:%s) captures %s request.",
      LoggingUtil.Layer.Resource.toString(),
      LoggingUtil.getCurrentMethod(),
      "id",
      int.class.getName(),
      "limit",
      int.class.getName(),
      LoggingUtil.HTTP.Get.toString()
    );
    List<EmployeeDepartmentAssignmentStatisticsDto> employeeDepartmentAssignmentStatisticsDtos =
      this.departmentService.getEmployeesWithLowestHoursSpentPerAssignmentWithinDepartmentInAscendingOrder(id, limit);
    Log.infof("Response Ok - Return a %s", EmployeeDepartmentAssignmentStatisticsDto.class.getName());
    return RestResponse.ok(employeeDepartmentAssignmentStatisticsDtos);
  }
}
