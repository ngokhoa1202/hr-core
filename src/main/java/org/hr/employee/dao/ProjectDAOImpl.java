package org.hr.employee.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.HibernateHints;
import org.hr.employee.entity.Assignment;
import org.hr.employee.entity.Project;

import java.util.Optional;

@ApplicationScoped
public class ProjectDAOImpl implements ProjectDAO{

  private final EntityManager entityManager;

  @Inject
  public ProjectDAOImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Optional<Project> findProjectById(Long id) {
    return Optional.ofNullable(this.entityManager.find(Project.class, id));
  }

  @Override
  public Optional<Project> saveProject(Project project) {
    this.entityManager.persist(project);
    this.entityManager.flush();
    return Optional.of(project);
  }

  @Override
  public Optional<Project> updateProject(Project project) {
    Project updatedProject = this.entityManager.merge(project);
    this.entityManager.flush();
    return Optional.of(updatedProject);
  }

  @Override
  public Optional<Integer> deleteProject(Long projectId) {
    Query query = this.entityManager.createNamedQuery("deleteProjectById");
    query.setParameter("id", projectId)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT);
    return Optional.of(Integer.valueOf(query.executeUpdate()));
  }
}
