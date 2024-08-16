package org.hr.security.service;

import org.hr.security.dto.*;

import java.util.UUID;

public interface AuthenticationService {
  JwtDTO login(UserLoginDTO userLoginDTO);

  UserDTO saveUser(UserCreationDTO userCreationDTO);
  UserDTO getUser(UUID id);

  RoleDTO saveRole(RoleCreationDTO roleCreationDTO);
  RoleDTO getRole(Integer id);
}
