package org.hr.security.service;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.hr.security.entity.Role;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JwtAuthenticationServiceImpl implements JwtAuthenticationService {

  private static final long DURATION_IN_SECONDS = 30 * 300;

  private long getJwtExpirationTime() {
    return System.currentTimeMillis() / 1000 + DURATION_IN_SECONDS;
  }

  public String generateJwtToken(Role role) {
    Set<String> userGroups = new HashSet<String>(
      Arrays.asList(role.getName())
    );

    return Jwt.issuer("company")
      .subject("human-resource")
      .groups(userGroups)
      .expiresAt(this.getJwtExpirationTime())
      .sign();
  }
}
