package org.hr.employee.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.hibernate.JDBCException;
import org.hr.employee.dto.project.ProjectAssignmentStatisticsDto;
import org.hr.employee.dto.project.assignment.AssignmentPayloadDto;
import org.hr.employee.dto.project.assignment.AssignmentResponseDto;
import org.hr.employee.dto.project.ProjectPayloadDto;
import org.hr.employee.dto.project.ProjectResponseDto;
import org.hr.employee.service.AssignmentService;
import org.hr.employee.service.ProjectService;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.converter.ExceptionConverter;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;
import java.util.List;

@Path("projects")
@ApplicationScoped
@RequiredArgsConstructor
public class ProjectResource {

  private final ProjectService projectService;
  private final AssignmentService assignmentService;
  private final ExceptionConverter exceptionConverter;

  private final String url = "api/projects";

  @Path("{id}")
  @GET
  public RestResponse<ProjectResponseDto> getProject(@RestPath(value = "id") Long id) throws EntityNotFoundException.HumanResourceException {
    ProjectResponseDto projectResponseDto = this.projectService.getProject(id);
    return RestResponse.ok(projectResponseDto);
  }

  @Path("")
  @POST
  public RestResponse<String> createProject(@RequestBody ProjectPayloadDto projectPayloadDto) throws EntityNotFoundException.HumanResourceException {
    try {
      ProjectResponseDto projectResponseDto = this.projectService.createProject(projectPayloadDto);
      return RestResponse.created(URI.create(this.url + projectResponseDto.id().toString()));
    } catch (JDBCException | ConstraintViolationException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("{id}")
  @PUT
  public RestResponse<String> updateProject(
    @RestPath(value = "id") Long projectId, @RequestBody ProjectPayloadDto projectPayloadDto
  ) throws EntityNotFoundException.HumanResourceException {
    try {
      ProjectResponseDto projectResponseDto = this.projectService.updateProject(projectId, projectPayloadDto);
      return RestResponse.created(URI.create(this.url + projectResponseDto.id().toString()));
    } catch (JDBCException | ConstraintViolationException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("{id}")
  @DELETE
  public RestResponse<String> deleteProject(@RestPath(value = "id") Long projectId) throws EntityNotFoundException.HumanResourceException {
    this.projectService.deleteProject(projectId);
    return RestResponse.noContent();
  }

  @Path("assignments")
  @POST
  public RestResponse<String> createAssignment(AssignmentPayloadDto assignmentPayloadDto)
    throws EntityNotFoundException.HumanResourceException {

    try {
      AssignmentResponseDto assignmentResponseDTO = this.assignmentService.createAssignment(assignmentPayloadDto);
      return RestResponse.created(URI.create(this.url + "assignments/" + assignmentResponseDTO.id().toString()));
    } catch (JDBCException | ConstraintViolationException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("assignments/{id}")
  @GET
  public RestResponse<AssignmentResponseDto> getAssignment(@RestPath(value = "id") Long id)
    throws EntityNotFoundException.HumanResourceException {

    AssignmentResponseDto assignmentResponseDto = this.assignmentService.getAssignment(id);
    return RestResponse.ok(assignmentResponseDto);
  }

  @Path("/assignments/{id}")
  @PUT
  public RestResponse<String> updateAssignment(
    @RestPath(value = "id") Long id, @RequestBody AssignmentPayloadDto assignmentPayloadDto
  ) throws EntityNotFoundException.HumanResourceException {

    try {
      AssignmentResponseDto assignmentResponseDTO = this.assignmentService.updateAssignment(id, assignmentPayloadDto);
      return RestResponse.created(URI.create(this.url + "/assignments" + assignmentResponseDTO.id().toString()));
    } catch (JDBCException | ConstraintViolationException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("assignments/{id}")
  @DELETE
  public RestResponse<String> deleteAssignment(@RestPath(value = "id") Long id) throws EntityNotFoundException.HumanResourceException {

    this.assignmentService.deleteAssignment(id);
    return RestResponse.noContent();
  }

  @Path("assignments/statistics/{limit}")
  @GET
  public RestResponse<List<ProjectAssignmentStatisticsDto>> getProjectsWithHighestHoursSpentInDescendingOrder(
    @RestPath("limit") int limit
  ) throws EntityNotFoundException.HumanResourceException {

    List<ProjectAssignmentStatisticsDto> projectAssignmentStatisticsDtos = this.projectService
      .getProjectsWithHighestHoursSpentInDescendingOrder(limit);
    return RestResponse.ok(projectAssignmentStatisticsDtos);
  }
}
