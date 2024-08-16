package org.hr.employee.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.hibernate.JDBCException;
import org.hr.employee.dto.AssignmentCreationDTO;
import org.hr.employee.dto.AssignmentDTO;
import org.hr.employee.dto.ProjectCreationDTO;
import org.hr.employee.dto.ProjectDTO;
import org.hr.employee.service.AssignmentService;
import org.hr.employee.service.ProjectService;
import org.hr.exception.InvalidRequestBodyException;
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
    ProjectDTO projectDTO = this.projectService.getProject(id);
    return RestResponse.ok(projectDTO);
  }

  @Path("{id}")
  @PUT
  public RestResponse<String> updateProject(
    @RestPath(value = "id") Long projectId, @RequestBody ProjectCreationDTO projectCreationDTO
  ) throws HumanResourceException {
    try {
      ProjectDTO updatedProjectDTO = this.projectService.updateProject(projectId, projectCreationDTO);
      return RestResponse.created(URI.create(updatedProjectDTO.id().toString()));
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
      AssignmentDTO assignmentDTO = this.assignmentService.createAssignment(
        assignmentCreationDTO.setProjectId(projectId)
      );
      return RestResponse.created(URI.create(assignmentDTO.id().toString()));
    } catch (JDBCException | ConstraintViolationException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("{projectId}/assignments/{assignmentId}")
  public RestResponse<AssignmentDTO> getAssignment(
    @RestPath(value = "projectId") Long projectId, @RestPath(value = "assignmentId") Long assignmentId
  ) throws HumanResourceException {

    AssignmentDTO assignmentDTO = this.assignmentService.getAssignment(assignmentId);
    return RestResponse.ok(assignmentDTO);
  }

  @Path("{projectId}/assignments/{assignmentId}")
  @PUT
  public RestResponse<String> updateAssignment(
    @RestPath(value = "projectId") Long projectId, @RestPath(value = "assignmentId") Long assignmentId,
    @RequestBody AssignmentCreationDTO assignmentCreationDTO
  ) throws HumanResourceException {

    try {
      AssignmentDTO assignmentDTO = this.assignmentService.updateAssignment(
        assignmentId, assignmentCreationDTO.setProjectId(projectId)
      );
      return RestResponse.created(URI.create(assignmentDTO.id().toString()));
    } catch (JDBCException | ConstraintViolationException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }
}
