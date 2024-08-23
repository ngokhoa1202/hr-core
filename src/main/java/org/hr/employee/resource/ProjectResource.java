package org.hr.employee.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.hibernate.JDBCException;
import org.hr.employee.dto.AssignmentCreationDTO;
import org.hr.employee.dto.AssignmentDTO;
import org.hr.employee.dto.ProjectCreationDTO;
import org.hr.employee.dto.ProjectDTO;
import org.hr.employee.service.AssignmentService;
import org.hr.employee.service.ProjectService;
import org.hr.exception.handler.ExceptionConverter;
import org.hr.exception.mapper.HumanResourceException;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;

@Path("projects")
@ApplicationScoped
public class ProjectResource {

  private final ProjectService projectService;
  private final AssignmentService assignmentService;
  private final ExceptionConverter exceptionConverter;

  @Inject
  public ProjectResource(
    ProjectService projectService, AssignmentService assignmentService, ExceptionConverter exceptionConverter) {

    this.projectService = projectService;
    this.assignmentService = assignmentService;
    this.exceptionConverter = exceptionConverter;
  }

  @Path("{id}")
  @GET
  public RestResponse<ProjectDTO> getProject(@RestPath(value = "id") Long id) throws HumanResourceException {

    return RestResponse.ok(
      this.projectService.getProject(id)
    );
  }

  @Path("{id}")
  @PUT
  public RestResponse<String> updateProject(
    @RestPath(value = "id") Long projectId, @RequestBody ProjectCreationDTO projectCreationDTO
  ) throws HumanResourceException {
    try {
      return RestResponse.created(
        URI.create(
          this.projectService.updateProject(projectId, projectCreationDTO)
            .id().toString()
        )
      );
    } catch (JDBCException | ConstraintViolationException ex) {
     throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("{id}")
  @DELETE
  public RestResponse<String> deleteProject(@RestPath(value = "id") Long projectId) throws HumanResourceException {
    this.projectService.deleteProject(projectId);
    return RestResponse.noContent();
  }

  @Path("{projectId}/assignments")
  @POST
  public RestResponse<String> createAssignment(
    @RestPath(value = "projectId") Long projectId, AssignmentCreationDTO assignmentCreationDTO)
    throws HumanResourceException {

    try {
      return RestResponse.created(
        URI.create(
          this.assignmentService.createAssignment(
            assignmentCreationDTO.setProjectId(projectId)
          ).id().toString()
        )
      );
    } catch (JDBCException | ConstraintViolationException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("{projectId}/assignments/{assignmentId}")
  public RestResponse<AssignmentDTO> getAssignment(
    @RestPath(value = "projectId") Long projectId, @RestPath(value = "assignmentId") Long assignmentId
  ) throws HumanResourceException {

    return RestResponse.ok(
      this.assignmentService.getAssignment(assignmentId)
    );
  }

  @Path("{projectId}/assignments/{assignmentId}")
  @PUT
  public RestResponse<String> updateAssignment(
    @RestPath(value = "projectId") Long projectId, @RestPath(value = "assignmentId") Long assignmentId,
    @RequestBody AssignmentCreationDTO assignmentCreationDTO
  ) throws HumanResourceException {

    try {
      return RestResponse.created(
        URI.create(
          this.assignmentService.updateAssignment(
            assignmentId, assignmentCreationDTO.setProjectId(projectId)
          ).id().toString()
        )
      );
    } catch (JDBCException | ConstraintViolationException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("{projectId}/assignments/{assignmentId}")
  @DELETE
  public RestResponse<String> deleteAssignment(
    @RestPath(value = "projectId") Long projectId, @RestPath(value = "assignmentId") Long assignmentId
  ) throws HumanResourceException {

    this.assignmentService.deleteAssignment(assignmentId);
    return RestResponse.noContent();
  }
}
