package org.hr.security.service;

import org.hr.security.entity.Role;

import java.util.Set;

public interface JwtAuthenticationService {

  String generateJwtToken(Role role);
}
