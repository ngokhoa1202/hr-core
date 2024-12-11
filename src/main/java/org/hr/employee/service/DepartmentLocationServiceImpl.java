package org.hr.employee.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.JDBCException;
import org.hr.employee.cache.CacheService;
import org.hr.employee.dao.DepartmentDao;
import org.hr.employee.dao.DepartmentLocationDao;
import org.hr.employee.dto.department.DepartmentMapper;
import org.hr.employee.dto.department.location.DepartmentLocationPayloadDto;
import org.hr.employee.dto.department.location.DepartmentLocationMapper;
import org.hr.employee.dto.department.location.DepartmentLocationResponseDTO;
import org.hr.employee.entity.Department;
import org.hr.employee.entity.DepartmentLocation;
import org.hr.exception.EntityNotFoundException;
import org.hr.exception.InvalidRequestBodyException;

import java.util.List;

@RequiredArgsConstructor
@ApplicationScoped
@Transactional
public class DepartmentLocationServiceImpl implements DepartmentLocationService {

  private final DepartmentLocationDao locationDAO;
  private final DepartmentDao departmentDAO;
  private final CacheService cacheService;

  @PostConstruct
  public void initialize() {
    this.cacheService.cacheDepartmentLocations();
  }

  @Override
  public DepartmentLocationResponseDTO getDepartmentLocation(@NonNull Long id) throws EntityNotFoundException {
    DepartmentLocation location = this.cacheService.getCachedDepartmentLocationById(id)
      .orElseThrow(() -> new EntityNotFoundException("id", DepartmentLocation.class.getSimpleName()));
    return DepartmentLocationMapper.INSTANCE.locationToLocationResponseDto(location);
  }

  @Override
  public DepartmentLocationResponseDTO createDepartmentLocation(
    @Valid DepartmentLocationPayloadDto locationPayloadDto
  ) throws ConstraintViolationException, JDBCException, InvalidRequestBodyException {

    DepartmentLocation location = DepartmentLocationMapper.INSTANCE.locationPayloadDtoToLocation(locationPayloadDto);
    Department departmentById = this.departmentDAO.findById(locationPayloadDto.departmentPlainDto().id())
      .orElseThrow(() -> new EntityNotFoundException("id", Department.class.getSimpleName()));

    Department departmentByDto = location.getDepartment();
    DepartmentService.ensureDepartmentIntegrity(departmentById, departmentByDto);

    location.setDepartment(departmentById);
    departmentById.getLocations().add(location);

    DepartmentLocation locationCreated = this.locationDAO.create(location)
      .orElseThrow(InvalidRequestBodyException::new);
    this.cacheService.evictCachedDepartmentLocations();

    return DepartmentLocationMapper.INSTANCE.locationToLocationResponseDto(locationCreated);
  }


  @Override
  public DepartmentLocationResponseDTO updateDepartmentLocation(
    @NonNull Long id, @Valid DepartmentLocationPayloadDto locationPayloadDto
  ) throws EntityNotFoundException, InvalidRequestBodyException, JDBCException {

    DepartmentLocation location = this.locationDAO.findById(id)
      .orElseThrow(() ->  new EntityNotFoundException("id", DepartmentLocation.class.getSimpleName()));

    Department oldDepartment = location.getDepartment();
    Department newDepartmentById = this.departmentDAO.findById(locationPayloadDto.departmentPlainDto().id())
      .orElseThrow(() -> new EntityNotFoundException("id", Department.class.getSimpleName()));

    Department newDepartmentByDto = DepartmentMapper.INSTANCE.departmentPlainDtoToDepartment(locationPayloadDto.departmentPlainDto());
    DepartmentService.ensureDepartmentIntegrity(newDepartmentById, newDepartmentByDto);

    location.setLocation(locationPayloadDto.location());
    location.setDepartment(newDepartmentById);
    oldDepartment.getLocations().remove(location);
    newDepartmentById.getLocations().add(location);

    DepartmentLocation locationUpdated = this.locationDAO.update(location)
      .orElseThrow(InvalidRequestBodyException::new);
    this.cacheService.evictCachedDepartmentLocations();
    return DepartmentLocationMapper.INSTANCE.locationToLocationResponseDto(locationUpdated);
  }

  @Override
  public void deleteDepartmentLocation(Long id) throws EntityNotFoundException {
    this.locationDAO.deleteById(id);
  }

  @Override
  public List<DepartmentLocationResponseDTO> getDepartmentLocations() {
    return this.locationDAO.findAll()
      .map(DepartmentLocationMapper.INSTANCE::locationToLocationResponseDto)
      .toList();
  }
}
