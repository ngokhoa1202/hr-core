package org.hr.config;


import io.quarkus.logging.Log;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.jpa.HibernateHints;
import org.hr.exception.EntityNotFoundException;


import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class BaseDAO<E> {

  private final Class<E> entityClass;
  public static final Integer QUERY_TIMEOUT = 2000;

  @PersistenceContext
  protected EntityManager entityManager;

  protected BaseDAO(Class<E> entityClass) {
    this.entityClass = entityClass;
  }

  public Optional<E> create(E entity)
    throws jakarta.validation.ConstraintViolationException, org.hibernate.exception.ConstraintViolationException {

    this.entityManager.persist(entity);
    this.entityManager.flush();
    return Optional.of(entity);
  }

  public Optional<E> update(E entity)
    throws jakarta.validation.ConstraintViolationException, org.hibernate.exception.ConstraintViolationException {

    E entityUpdated = this.entityManager.merge(entity);
    this.entityManager.flush();
    return Optional.of(entityUpdated);
  }

  public Optional<E> findById(Integer id) {
    return Optional.ofNullable(this.entityManager.find(entityClass, id));
  }

  public Optional<E> findById(Long id) {
    return Optional.ofNullable(this.entityManager.find(entityClass, id));
  }

  public Optional<E> findById(UUID id) {
    return Optional.ofNullable(this.entityManager.find(entityClass, id));
  }

  public Stream<E> findAll(int startIndex, int limit) {
    CriteriaQuery<E> criteriaQuery = this.entityManager.getCriteriaBuilder()
      .createQuery(this.entityClass);
    Root<E> root = criteriaQuery.from(this.entityClass);
    criteriaQuery.select(root);

    return this.entityManager.createQuery(criteriaQuery)
      .setFirstResult(startIndex)
      .setMaxResults(limit)
      .setHint(HibernateHints.HINT_READ_ONLY, true)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT)
      .getResultStream();
  }

  public Stream<E> findAll() {

    CriteriaQuery<E> criteriaQuery = this.entityManager.getCriteriaBuilder()
      .createQuery(this.entityClass);
    Root<E> root = criteriaQuery.from(this.entityClass);
    criteriaQuery.select(root);

    return this.entityManager.createQuery(criteriaQuery)
      .setHint(HibernateHints.HINT_READ_ONLY, true)
      .setHint(HibernateHints.HINT_TIMEOUT, QUERY_TIMEOUT)
      .getResultStream();
  }

  public void deleteById(Long id) throws EntityNotFoundException {
    E entityDeleted = this.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("id", this.entityClass.getSimpleName()));
    this.entityManager.remove(entityDeleted);
  }

  public void deleteById(UUID id) throws EntityNotFoundException {

    E entityDeleted = this.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("id", this.entityClass.getSimpleName()));
    this.entityManager.remove(entityDeleted);
  }

  public Long countTotal() {

    CriteriaQuery<Long> criteriaQuery = this.entityManager.getCriteriaBuilder()
      .createQuery(Long.class);
    Root<E> root = criteriaQuery.from(this.entityClass);
    criteriaQuery.select(this.entityManager.getCriteriaBuilder().count(root));
    return this.entityManager.createQuery(criteriaQuery)
      .getSingleResult();
  }
}
