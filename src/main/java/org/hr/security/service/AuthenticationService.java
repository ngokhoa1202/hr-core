package org.hr.security.service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.hibernate.JDBCException;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;
import org.hr.exception.UnauthorizedException;
import org.hr.security.dto.*;
import org.hr.security.dto.role.RolePayloadDto;
import org.hr.security.dto.role.RoleResponseDto;
import org.hr.security.dto.user.UserPayloadDto;
import org.hr.security.dto.user.UserLoginDto;
import org.hr.security.dto.user.UserResponseDto;

import java.util.UUID;

public interface AuthenticationService {

  JwtDto login(@Valid UserLoginDto userLoginDTO)
    throws EntityNotFoundException, UnauthorizedException, InvalidRequestBodyException;

  UserResponseDto createUser(@Valid UserPayloadDto userCreationDTO)
    throws EntityNotFoundException, InvalidRequestBodyException, JDBCException, ConstraintViolationException;

  UserResponseDto getUser(UUID id) throws EntityNotFoundException;

  RoleResponseDto createRole(@Valid RolePayloadDto rolePayloadDto) throws ConstraintViolationException, JDBCException;
  RoleResponseDto getRole(Long id) throws EntityNotFoundException;
}
