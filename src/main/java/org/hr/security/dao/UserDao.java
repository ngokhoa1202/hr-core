package org.hr.security.dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;
import org.hr.config.BaseDAO;
import org.hr.security.entity.User;

import java.util.Optional;

@RequestScoped
public class UserDao extends BaseDAO<User> {

  public UserDao() {
    super(User.class);
  }

  public Optional<User> findByUsername(String username) throws NoResultException {
    return Optional.of(
      this.entityManager.createNamedQuery("findUserByUsername", User.class)
        .setParameter("username", username)
        .getSingleResult()
    );
  }
}
