package org.hr.security.service;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.hr.security.entity.Role;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JwtAuthenticationServiceImpl implements JwtAuthenticationService{



  public String generateJwtToken(Role role) {
    Set<String> userGroups = new HashSet<String>(
      Arrays.asList(role.getName())
    );

    return Jwt.issuer("company")
      .subject("human-resource")
      .groups(userGroups)
      .expiresAt(
        System.currentTimeMillis() + 10*300*1000
      )
      .sign();
  }
}
