package org.hr.security.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;
import org.hr.exception.UnauthorizedException;
import org.hr.exception.handler.ExceptionConverter;
import org.hr.security.dao.AuthenticationDAO;
import org.hr.security.dto.*;
import org.hr.security.entity.Role;
import org.hr.security.entity.User;

import java.util.UUID;

@Singleton
public class AuthenticationServiceImpl implements AuthenticationService {

  private AuthenticationDAO authenticationDAO;
  private ExceptionConverter handler;
  private JwtAuthenticationService jwtAuthentication;
  private PasswordBasedAuthenticationService passwordBasedAuthenticationService;


  @Inject
  public AuthenticationServiceImpl(
    AuthenticationDAO authenticationDAO, JwtAuthenticationService jwtAuthentication,
    PasswordBasedAuthenticationService passwordBasedAuthenticationService) {

    this.authenticationDAO = authenticationDAO;
    this.jwtAuthentication = jwtAuthentication;
    this.passwordBasedAuthenticationService = passwordBasedAuthenticationService;
  }

  @Inject
  public void setExceptionHandler(ExceptionConverter handler) {
    this.handler = handler;
  }

  @Override
  @Transactional
  public JwtDTO login(UserLoginDTO userLoginDTO) throws EntityNotFoundException, UnauthorizedException, InvalidRequestBodyException {
    User user = this.authenticationDAO.findUserByUsername(userLoginDTO.username());
    if (user == null) {
      throw new EntityNotFoundException("user");
    }
    boolean verified = this.passwordBasedAuthenticationService.authenticate(userLoginDTO.password(), user.getPassword());
    if (! verified) {
      throw new UnauthorizedException();
    }

    String jwt = this.jwtAuthentication.generateJwtToken(user.getRole());
    return new JwtDTO(jwt);
  }

  @Override
  @Transactional
  public UserDTO saveUser(UserCreationDTO userCreationDTO) throws EntityNotFoundException {
    Role role = null;
    try {
      role = this.authenticationDAO.findRoleByName(userCreationDTO.roleName());
    } catch (NoResultException ex) {
      this.handler.convert(ex);
    }

    User user = this.passwordBasedAuthenticationService.encrypt(userCreationDTO.toUser().setRole(role));
    User savedUser = null;
    try {
      savedUser = this.authenticationDAO.saveUser(user);
    } catch (RuntimeException ex) {
      this.handler.convert(ex);
    }

    return savedUser.toUserDTO();
  }

  @Override
  @Transactional
  public UserDTO getUser(UUID id) throws EntityNotFoundException {
    User user = this.authenticationDAO.findUserById(id);
    if (user == null) {
      throw new EntityNotFoundException("user");
    }
    return user.toUserDTO();
  }

  @Override
  @Transactional
  public RoleDTO saveRole(RoleCreationDTO roleCreationDTO) {
    Role role = roleCreationDTO.toRole();
    Role savedRole = null;
    try {
      savedRole = this.authenticationDAO.saveRole(role);
    } catch (RuntimeException ex) {
      this.handler.convert(ex);
    }

    return savedRole.toRoleDTO();
  }

  @Override
  @Transactional
  public RoleDTO getRole(Integer id) throws EntityNotFoundException {
    Role role = this.authenticationDAO.findRoleById(id);
    if (role == null) {
      throw new EntityNotFoundException("role");
    }
    return role.toRoleDTO();
  }
}
