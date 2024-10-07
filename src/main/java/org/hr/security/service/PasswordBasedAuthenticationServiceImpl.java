package org.hr.security.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.hr.security.entity.User;

import java.util.Optional;

@ApplicationScoped
public class PasswordBasedAuthenticationServiceImpl implements PasswordBasedAuthenticationService {

  @Override
  public Optional<Boolean> authenticate(String plainPassword, String hashPassword)  {
    return Optional.of(BcryptUtil.matches(plainPassword, hashPassword));
  }


  @Override
  public User encrypt(User user) {

    String password = BcryptUtil.bcryptHash(user.getPassword());
    user.setPassword(password);
    return user;
  }
}
