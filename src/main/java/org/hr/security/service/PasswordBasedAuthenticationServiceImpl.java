package org.hr.security.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import org.hr.exception.InvalidRequestBodyException;
import org.hr.security.entity.User;

@ApplicationScoped
public class PasswordBasedAuthenticationServiceImpl implements PasswordBasedAuthenticationService {

  @Override
  public boolean authenticate(String plainPassword, String hashPassword)  {
    return BcryptUtil.matches(plainPassword, hashPassword);
  }


  @Override
  public User encrypt(User user) throws InvalidRequestBodyException {
    return user.setPassword(BcryptUtil.bcryptHash(user.getPassword()));
  }
}
