package org.hr.security.dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;
import org.hr.config.BaseDAO;
import org.hr.security.entity.Role;

import java.util.Optional;

@RequestScoped
public class RoleDao extends BaseDAO<Role> {

  public RoleDao() {
    super(Role.class);
  }

  public Optional<Role> findByName(String name) throws NoResultException {
    return Optional.of(
      this.entityManager.createNamedQuery("findRoleByName", Role.class)
        .setParameter("name", name)
        .getSingleResult()
    );
  }
}
