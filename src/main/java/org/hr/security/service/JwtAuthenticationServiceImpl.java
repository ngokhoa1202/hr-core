package org.hr.security.service;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.hr.security.entity.Role;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JwtAuthenticationServiceImpl implements JwtAuthenticationService {

  private static final long EXPIRED_TOKEN_TIME_IN_HOUR = 1;


  public String generateJwtToken(Role role) {
    Set<String> userGroups = new HashSet<String>(
      Arrays.asList(role.getName())
    );

    return Jwt.issuer("company")
      .subject("human-resource")
      .groups(userGroups)
      .expiresIn(Duration.ofHours(EXPIRED_TOKEN_TIME_IN_HOUR))
      .sign();
  }
}
