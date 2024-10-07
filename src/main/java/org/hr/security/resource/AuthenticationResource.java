package org.hr.security.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.hibernate.JDBCException;
import org.hr.exception.handler.ExceptionConverter;
import org.hr.exception.mapper.HumanResourceException;
import org.hr.security.dto.*;
import org.hr.security.dto.role.RolePayloadDto;
import org.hr.security.dto.role.RoleResponseDto;
import org.hr.security.dto.user.UserPayloadDto;
import org.hr.security.dto.user.UserLoginDto;
import org.hr.security.dto.user.UserResponseDto;
import org.hr.security.service.AuthenticationService;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;
import java.util.UUID;

@ApplicationScoped
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@RequiredArgsConstructor
@Path("security")
public class AuthenticationResource {

  private final AuthenticationService authenticationService;
  private final ExceptionConverter exceptionConverter;


  @PermitAll
  @Path("login")
  @POST
  public RestResponse<JwtDto> getJwtToken(@RequestBody UserLoginDto userLoginDTO)
    throws HumanResourceException {

    JwtDto jwtDTO = this.authenticationService.login(userLoginDTO);
    return RestResponse.ok(jwtDTO);
  }

  @Path("user")
  @POST
  public RestResponse<String> createUser(@RequestBody UserPayloadDto userPayloadDto)
    throws HumanResourceException {

    try {
      UserResponseDto userResponseDTO = this.authenticationService.createUser(userPayloadDto);
      return RestResponse.created(URI.create(userResponseDTO.id().toString()));
    } catch (ConstraintViolationException | JDBCException ex) {
      throw this.exceptionConverter.convert(ex);
    }
  }

  @Path("user/{id}")
  @GET
  @RolesAllowed({"admin"})
  public RestResponse<UserResponseDto> getUser(@RestPath(value = "id") UUID id) throws HumanResourceException {
    UserResponseDto userResponseDTO = this.authenticationService.getUser(id);
    return RestResponse.ok(userResponseDTO);
  }

  @Path("role/{id}")
  @GET
  @RolesAllowed({"admin"})
  public RestResponse<RoleResponseDto> getRole(@RestPath(value = "id") Long id) throws HumanResourceException {
    RoleResponseDto roleResponseDTO = this.authenticationService.getRole(id);
    return RestResponse.ok(roleResponseDTO);
  }

  @Path("role")
  @POST
  @RolesAllowed({"admin"})
  public RestResponse<String> createRole(@RequestBody RolePayloadDto rolePayloadDto) throws HumanResourceException {
    try {
      RoleResponseDto roleResponseDTO = this.authenticationService.createRole(rolePayloadDto);
      return RestResponse.created(URI.create(roleResponseDTO.id().toString()));
    } catch (JDBCException | ConstraintViolationException ex) {
      throw this.exceptionConverter.convert(ex);
    }

  }
}
