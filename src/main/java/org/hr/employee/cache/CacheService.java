package org.hr.employee.cache;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.hr.employee.dao.DepartmentDao;
import org.hr.employee.dao.DepartmentLocationDao;
import org.hr.employee.dao.ProjectDao;
import org.hr.employee.entity.Department;
import org.hr.employee.entity.DepartmentLocation;
import org.hr.employee.entity.Project;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.Optional;

/**
 * The cache service employs read through and write aside mechanism
 * app <-> cache <-> db for read
 * app --> db --> clear cache for write
 *
 */
@Singleton
@RequiredArgsConstructor
public class CacheService {

  @CacheName("app-cache")
  protected Cache cache;

  private final DepartmentDao departmentDAO;
  private final DepartmentLocationDao locationDAO;
  private final ProjectDao projectDAO;

  public static final String DEPARTMENTS_KEY = "departments";
  public static final String DEPARTMENT_LOCATIONS_KEY = "locations";
  public static final String PROJECTS_KEY = "projects";


  public void cacheDepartments() {
    
    cache.as(CaffeineCache.class).put(
      DEPARTMENTS_KEY,
      CompletableFuture.completedFuture(
        this.departmentDAO.findAll()
          .distinct()
          .collect(
            Collectors.toMap(
              Department::getId,
              (department) -> department
            )
          )
      )
    );
  }

  public void cacheDepartmentLocations() {
    cache.as(CaffeineCache.class).put(DEPARTMENT_LOCATIONS_KEY, CompletableFuture.completedFuture(
      this.locationDAO.findAll()
        .distinct()
        .collect(
          Collectors.toMap(
            DepartmentLocation::getId,
            (location) -> location
          )
        )
    ));
  }

  public void cacheProjects() {
    cache.as(CaffeineCache.class).put(
      PROJECTS_KEY,
      CompletableFuture.completedFuture(
        this.projectDAO.findAll()
          .distinct()
          .collect(
            Collectors.toMap(
              Project::getId,
              (project) -> project
            )
          )
      ));
  }

  public Map<Long, Department> getCachedDepartments() {

    return this.cache.get(DEPARTMENTS_KEY, (key) -> {

      return this.departmentDAO.findAll()
        .collect(
          Collectors.toMap(
            Department::getId,
            (d) -> d
          )
        );
    }).await().atMost(Duration.ofMillis(500));
  }

  protected void evictCache(String key) {
    this.cache.invalidate(key);
  }

  public void evictCachedDepartments() {
    this.evictCache(DEPARTMENTS_KEY);
  }

  public void evictCachedDepartmentLocations() {
    this.evictCache(DEPARTMENT_LOCATIONS_KEY);
  }

  public void evictCachedProjects() {
    this.evictCache(PROJECTS_KEY);
  }


  public Map<Long, DepartmentLocation> getCachedDepartmentLocations() {

    return this.cache.get(DEPARTMENT_LOCATIONS_KEY, (key) -> {

      return this.locationDAO.findAll()
        .distinct()
        .collect(
          Collectors.toMap(
            DepartmentLocation::getId,
            (location) -> location
          )
        );
    }).await().atMost(Duration.ofMillis(500));
  }

  public Map<Long, Project> getCachedProjects() {

    return this.cache.get(PROJECTS_KEY, (key) -> {
      return this.projectDAO.findAll()
        .distinct()
        .collect(
          Collectors.toMap(
            Project::getId,
            (project) -> project
          )
        );
    }).await().atMost(Duration.ofMillis(500));
  }

  public Optional<Department> getCachedDepartmentById(Long id) {
    return Optional.ofNullable(this.getCachedDepartments().get(id));
  }

  public Optional<Project> getCachedProjectById(Long id) {
    return Optional.ofNullable(this.getCachedProjects().get(id));
  }

  public Optional<DepartmentLocation> getCachedDepartmentLocationById(Long id) {
    return Optional.ofNullable(this.getCachedDepartmentLocations().get(id));
  }
}
