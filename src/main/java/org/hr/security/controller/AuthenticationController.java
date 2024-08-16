package org.hr.security.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.NonNull;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.hr.exception.DuplicateFieldException;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;
import org.hr.exception.UnauthorizedException;
import org.hr.security.dto.*;
import org.hr.security.service.AuthenticationServiceImpl;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;
import java.util.UUID;

@Path("api")
@ApplicationScoped
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
class AuthenticationController {

  private final AuthenticationServiceImpl authenticationService;

  @Inject
  public AuthenticationController(@NonNull final AuthenticationServiceImpl jwtService) {
    this.authenticationService = jwtService;
  }

  @PermitAll
  @Path("login")
  @POST
  public RestResponse<JwtDTO> getJwtToken(@RequestBody UserLoginDTO userLoginDTO) throws UnauthorizedException, EntityNotFoundException {
    JwtDTO jwt = this.authenticationService.login(userLoginDTO);
    return RestResponse.ok(jwt);
  }

  @Path("user")
  @POST
  public RestResponse<String> createUser(@RequestBody UserCreationDTO userCreationDTO)
    throws EntityNotFoundException, DuplicateFieldException, InvalidRequestBodyException {

    UserDTO userDTO = this.authenticationService.saveUser(userCreationDTO);
    return RestResponse.created(URI.create(userDTO.getId().toString()));
  }

  @Path("user/{id}")
  @GET
  @RolesAllowed({"admin"})
  public RestResponse<UserDTO> getUser(@RestPath(value = "id") UUID id) {
    UserDTO userDTO = this.authenticationService.getUser(id);
    return RestResponse.ok(userDTO);
  }

  @Path("role/{id}")
  @GET
  @RolesAllowed({"admin"})
  public RestResponse<RoleDTO> getRole(@RestPath(value = "id") Integer id) {
    RoleDTO roleDTO = this.authenticationService.getRole(id);
    return RestResponse.ok(roleDTO);
  }

  @Path("role")
  @POST
  @RolesAllowed({"admin"})
  public RestResponse<String> saveRole(@RequestBody RoleCreationDTO roleCreationDTO) {
    RoleDTO roleDTO = this.authenticationService.saveRole(roleCreationDTO);
    return RestResponse.created(URI.create(roleDTO.getId().toString()));
  }
}
