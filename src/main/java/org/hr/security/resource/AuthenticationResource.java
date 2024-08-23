package org.hr.security.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.NonNull;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.hibernate.JDBCException;
import org.hr.exception.handler.ExceptionConverter;
import org.hr.exception.mapper.HumanResourceException;
import org.hr.security.dto.*;
import org.hr.security.service.AuthenticationService;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;
import java.util.UUID;

@ApplicationScoped
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Path("security")
public class AuthenticationResource {

  private final AuthenticationService authenticationService;
  private final ExceptionConverter exceptionConverter;

  @Inject
  public AuthenticationResource(
    @NonNull final AuthenticationService authenticationService, @NonNull final ExceptionConverter exceptionConverter) {

    this.authenticationService = authenticationService;
    this.exceptionConverter = exceptionConverter;
  }

  @PermitAll
  @Path("login")
  @POST
  public RestResponse<JwtDTO> getJwtToken(@RequestBody UserLoginDTO userLoginDTO)
    throws HumanResourceException {

    JwtDTO jwtDTO = this.authenticationService.login(userLoginDTO);
    return RestResponse.ok(jwtDTO);
  }

  @Path("user")
  @POST
  public RestResponse<String> createUser(@RequestBody UserCreationDTO userCreationDTO)
    throws HumanResourceException {

    try {
      return RestResponse.created(
        URI.create(
          this.authenticationService.saveUser(userCreationDTO).getId().toString()
        )
      );
    } catch (ConstraintViolationException | JDBCException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("user/{id}")
  @GET
  @RolesAllowed({"admin"})
  public RestResponse<UserDTO> getUser(@RestPath(value = "id") UUID id) throws HumanResourceException {
    return RestResponse.ok(
      this.authenticationService.getUser(id)
    );
  }

  @Path("role/{id}")
  @GET
  @RolesAllowed({"admin"})
  public RestResponse<RoleDTO> getRole(@RestPath(value = "id") Integer id) throws HumanResourceException {
    return RestResponse.ok(
      this.authenticationService.getRole(id)
    );
  }

  @Path("role")
  @POST
  @RolesAllowed({"admin"})
  public RestResponse<String> saveRole(@RequestBody RoleCreationDTO roleCreationDTO) throws HumanResourceException {
    try {
      return RestResponse.created(
        URI.create(
          this.authenticationService.saveRole(roleCreationDTO).getId().toString()
        )
      );
    } catch (JDBCException | ConstraintViolationException ex) {
      throw this.exceptionConverter.convert(ex);
    }

  }
}
