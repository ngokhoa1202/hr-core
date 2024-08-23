package org.hr.security.dao;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import org.hibernate.JDBCException;
import org.hr.security.entity.Role;
import org.hr.security.entity.User;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class AuthenticationDAOImpl implements AuthenticationDAO {

  private EntityManager entityManager;

  @Inject
  public AuthenticationDAOImpl(@NonNull final EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Optional<User> saveUser(User user) throws ConstraintViolationException, JDBCException {
    this.entityManager.persist(user);
    this.entityManager.flush();
    return Optional.of(user);
  }

  @Override
  public Optional<Role> saveRole(Role role) throws ConstraintViolationException, JDBCException {
    this.entityManager.persist(role);
    this.entityManager.flush();
    return Optional.of(role);
  }

  @Override
  public Optional<User> findUserById(UUID id) {
    return Optional.ofNullable(this.entityManager.find(User.class, id));
  }

  @Override
  public Optional<User> findUserByUsername(String username) throws NoResultException {
    return Optional.of(
      this.entityManager.createNamedQuery("findUserByUsername", User.class)
        .setParameter("username", username)
        .getSingleResult()
    );
  }

  @Override
  public Optional<Role> findRoleByName(String name) throws NoResultException {
    return Optional.of(
      this.entityManager.createNamedQuery("findRoleByName", Role.class)
        .setParameter("name", name)
        .getSingleResult()
    );
  }

  @Override
  public Optional<Role> findRoleById(Integer id) {
    return Optional.ofNullable(this.entityManager.find(Role.class, id));
  }
}
