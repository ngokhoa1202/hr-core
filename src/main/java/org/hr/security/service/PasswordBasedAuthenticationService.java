package org.hr.security.service;

import jakarta.inject.Singleton;
import org.hr.security.entity.Role;
import org.hr.security.entity.User;

import java.security.NoSuchAlgorithmException;

public interface PasswordBasedAuthenticationService {

  User encrypt(User user);
  boolean authenticate(String plainPassword, String hashPassword);
}
