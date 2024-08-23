package org.hr.security.service;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.JDBCException;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;
import org.hr.exception.UnauthorizedException;
import org.hr.security.dao.AuthenticationDAO;
import org.hr.security.dto.*;
import org.hr.security.entity.Role;
import org.hr.security.entity.User;

import java.util.UUID;

@Singleton
public class AuthenticationServiceImpl implements AuthenticationService {

  private final AuthenticationDAO authenticationDAO;
  private final JwtAuthenticationService jwtAuthentication;
  private final PasswordBasedAuthenticationService passwordBasedAuthenticationService;

  @Inject
  public AuthenticationServiceImpl(
    AuthenticationDAO authenticationDAO, JwtAuthenticationService jwtAuthentication,
    PasswordBasedAuthenticationService passwordBasedAuthenticationService) {

    this.authenticationDAO = authenticationDAO;
    this.jwtAuthentication = jwtAuthentication;
    this.passwordBasedAuthenticationService = passwordBasedAuthenticationService;
  }


  @Override
  @Transactional
  @PermitAll
  public JwtDTO login(UserLoginDTO userLoginDTO)
    throws EntityNotFoundException, UnauthorizedException, InvalidRequestBodyException {

    User user = this.authenticationDAO.findUserByUsername(userLoginDTO.username())
      .orElseThrow(InvalidRequestBodyException::new);
    this.passwordBasedAuthenticationService.authenticate(
      userLoginDTO.password(), user.getPassword()
    ).filter((verified) -> verified).orElseThrow(UnauthorizedException::new);

    return new JwtDTO(this.jwtAuthentication.generateJwtToken(user.getRole()));
  }

  @Override
  @Transactional
  @RolesAllowed({"admin"})
  public UserDTO saveUser(UserCreationDTO userCreationDTO)
    throws EntityNotFoundException, InvalidRequestBodyException, ConstraintViolationException, JDBCException {

    return this.authenticationDAO.saveUser(
      userCreationDTO.toUser()
        .setRole(
          this.authenticationDAO.findRoleByName(userCreationDTO.roleName())
            .orElseThrow(InvalidRequestBodyException::new)
        )
    ).orElseThrow(InvalidRequestBodyException::new).toUserDTO();
  }

  @Override
  @Transactional
  @RolesAllowed({"admin"})
  public UserDTO getUser(UUID id) throws EntityNotFoundException {
    return this.authenticationDAO.findUserById(id)
      .orElseThrow(() -> new EntityNotFoundException(User.class.getName()))
      .toUserDTO();
  }

  @Override
  @Transactional
  @RolesAllowed({"admin"})
  public RoleDTO saveRole(RoleCreationDTO roleCreationDTO) throws ConstraintViolationException, JDBCException {
    return this.authenticationDAO.saveRole(roleCreationDTO.toRole())
      .orElseThrow(InvalidRequestBodyException::new)
      .toRoleDTO();
  }

  @Override
  @Transactional
  @RolesAllowed({"admin", "user"})
  public RoleDTO getRole(Integer id) throws EntityNotFoundException {
    return this.authenticationDAO.findRoleById(id)
      .orElseThrow(() -> new EntityNotFoundException(Role.class.getName()))
      .toRoleDTO();
  }
}
