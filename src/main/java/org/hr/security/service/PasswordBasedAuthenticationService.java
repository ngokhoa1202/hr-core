package org.hr.security.service;

import org.hr.security.entity.User;

import java.util.Optional;

public interface PasswordBasedAuthenticationService {

  User encrypt(User user);
  Optional<Boolean> authenticate(String plainPassword, String hashPassword);
}
