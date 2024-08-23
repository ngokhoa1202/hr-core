package org.hr.security.service;

import jakarta.validation.ConstraintViolationException;
import org.hibernate.JDBCException;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;
import org.hr.exception.UnauthorizedException;
import org.hr.security.dto.*;

import java.util.UUID;

public interface AuthenticationService {

  JwtDTO login(UserLoginDTO userLoginDTO)
    throws EntityNotFoundException, UnauthorizedException, InvalidRequestBodyException;

  UserDTO saveUser(UserCreationDTO userCreationDTO)
    throws EntityNotFoundException, InvalidRequestBodyException, JDBCException, ConstraintViolationException;

  UserDTO getUser(UUID id) throws EntityNotFoundException;

  RoleDTO saveRole(RoleCreationDTO roleCreationDTO) throws ConstraintViolationException, JDBCException;
  RoleDTO getRole(Integer id) throws EntityNotFoundException;
}
