package org.hr.security.dao;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import org.hr.security.entity.Role;
import org.hr.security.entity.User;

import java.util.UUID;

@Singleton
public class AuthenticationDAOImpl implements AuthenticationDAO {

  private EntityManager entityManager;

  @Inject
  public AuthenticationDAOImpl(@NonNull final EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public User saveUser(User user) throws ConstraintViolationException {
    this.entityManager.persist(user);
    this.entityManager.flush();
    return user;
  }

  @Override
  public Role saveRole(Role role) {
    this.entityManager.persist(role);
    this.entityManager.flush();
    return role;
  }

  @Override
  public User findUserById(UUID id) {
    return this.entityManager.find(User.class, id);
  }

  @Override
  public User findUserByUsername(String username) throws NoResultException {
    TypedQuery<User> query = this.entityManager.createNamedQuery("findUserByUsername", User.class);
    query.setParameter("username", username);
    return query.getSingleResult();
  }

  @Override
  public Role findRoleByName(String name) throws NoResultException {
    TypedQuery<Role> query = this.entityManager.createNamedQuery("findRoleByName", Role.class);
    query.setParameter("name", name);
    return query.getSingleResult();
  }

  @Override
  public Role findRoleById(Integer id) {
    return this.entityManager.find(Role.class, id);
  }
}
