package org.hr.security.dao;

import jakarta.persistence.NoResultException;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.JDBCException;
import org.hr.security.entity.Role;
import org.hr.security.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface AuthenticationDAO {

  Optional<User> saveUser(User user) throws ConstraintViolationException, JDBCException;
  Optional<User> findUserById(UUID id);
  Optional<User> findUserByUsername(String username) throws NoResultException;

  Optional<Role> saveRole(Role role) throws ConstraintViolationException, JDBCException;
  Optional<Role> findRoleByName(String name) throws NoResultException;
  Optional<Role> findRoleById(Integer id);

}
