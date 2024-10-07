package org.hr.security.service;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.JDBCException;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;
import org.hr.exception.UnauthorizedException;
import org.hr.security.dao.RoleDao;
import org.hr.security.dao.UserDao;
import org.hr.security.dto.*;
import org.hr.security.dto.role.RoleMapper;
import org.hr.security.dto.role.RolePayloadDto;
import org.hr.security.dto.role.RoleResponseDto;
import org.hr.security.dto.user.UserMapper;
import org.hr.security.dto.user.UserPayloadDto;
import org.hr.security.dto.user.UserLoginDto;
import org.hr.security.dto.user.UserResponseDto;
import org.hr.security.entity.Role;
import org.hr.security.entity.User;

import java.util.UUID;

@Singleton
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserDao userDAO;
  private final RoleDao roleDAO;
  private final JwtAuthenticationService jwtAuthentication;
  private final PasswordBasedAuthenticationService passwordBasedAuthenticationService;

  @Override
  public JwtDto login(@Valid UserLoginDto userLoginDto)
    throws EntityNotFoundException, UnauthorizedException, InvalidRequestBodyException {

    User user = this.userDAO.findByUsername(userLoginDto.username())
      .orElseThrow(() -> new EntityNotFoundException("username", User.class.getName()));

    this.passwordBasedAuthenticationService.authenticate(userLoginDto.password(), user.getPassword())
      .filter((verified) -> verified)
      .orElseThrow(() -> new UnauthorizedException("user"));

    return new JwtDto(this.jwtAuthentication.generateJwtToken(user.getRole()));
  }

  @Override
  public UserResponseDto createUser(@Valid UserPayloadDto userPayloadDto)
    throws EntityNotFoundException, InvalidRequestBodyException, ConstraintViolationException, JDBCException {

    User user = UserMapper.INSTANCE.userPayloadDtoToUser(userPayloadDto);


    User userCreated = this.userDAO.create(user)
      .orElseThrow(InvalidRequestBodyException::new);
    return UserMapper.INSTANCE.userToUserResponseDto(userCreated);
  }

  @Override
  public UserResponseDto getUser(UUID id) throws EntityNotFoundException {
    User user = this.userDAO.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("id", User.class.getSimpleName()));
    return UserMapper.INSTANCE.userToUserResponseDto(user);
  }

  @Override
  public RoleResponseDto createRole(@Valid RolePayloadDto rolePayloadDto) throws ConstraintViolationException, JDBCException {
    Role role = RoleMapper.INSTANCE.rolePayloadDtoToRole(rolePayloadDto);
    Role roleCreated = this.roleDAO.create(role)
      .orElseThrow(InvalidRequestBodyException::new);

    return RoleMapper.INSTANCE.roleToRoleResponseDto(roleCreated);
  }

  @Override
  public RoleResponseDto getRole(Long id) throws EntityNotFoundException {
    Role role = this.roleDAO.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("id", Role.class.getSimpleName()));
    return RoleMapper.INSTANCE.roleToRoleResponseDto(role);
  }
}
