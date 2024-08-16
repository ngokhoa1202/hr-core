package org.hr.security.dao;

import org.hr.security.entity.Role;
import org.hr.security.entity.User;

import java.util.UUID;

public interface AuthenticationDAO {

  User saveUser(User user);
  User findUserById(UUID id);
  User findUserByUsername(String username);

  Role saveRole(Role role);
  Role findRoleByName(String name);
  Role findRoleById(Integer id);

}
