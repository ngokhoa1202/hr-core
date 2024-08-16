package org.hr.employee.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.JDBCException;
import org.hr.employee.dao.DepartmentDAO;
import org.hr.employee.dto.DepartmentCreationDTO;
import org.hr.employee.dto.DepartmentDTO;
import org.hr.employee.dto.DepartmentLocationCreationDTO;
import org.hr.employee.dto.DepartmentLocationDTO;
import org.hr.employee.entity.Department;
import org.hr.employee.entity.DepartmentLocation;
import jakarta.validation.ConstraintViolationException;
import org.hr.exception.*;

@ApplicationScoped
public class DepartmentServiceImpl implements DepartmentService {

  private DepartmentDAO departmentDAO;

  @Inject
  public DepartmentServiceImpl(DepartmentDAO departmentDAO) {
    this.departmentDAO = departmentDAO;
  }

  @Override
  @Transactional
  public DepartmentDTO saveDepartment(DepartmentCreationDTO departmentCreationDTO)
    throws ConstraintViolationException, InvalidRequestBodyException, JDBCException {

    return this.departmentDAO.saveDepartment(
      departmentCreationDTO.toDepartment()
    ).orElseThrow(InvalidRequestBodyException::new).toDepartmentDTO();
  }

  @Override
  @Transactional
  public void deleteDepartment(Long id) throws EntityNotFoundException {
    this.departmentDAO.deleteDepartment(id)
      .filter((rowsUpdated) -> rowsUpdated == 1)
      .orElseThrow(() -> new EntityNotFoundException(Department.class.getName()));
  }

  @Override
  public DepartmentLocationDTO getDepartmentLocation(Long id) throws EntityNotFoundException {
    return this.departmentDAO.findDepartmentLocationById(id)
      .orElseThrow(() -> new EntityNotFoundException(DepartmentLocation.class.getName()))
      .toDepartmentLocationDTO();
  }

  @Override
  @Transactional
  public DepartmentLocationDTO saveDepartmentLocation(
    @NonNull DepartmentLocationCreationDTO departmentLocationCreationDTO
  ) throws EntityNotFoundException, NoResultException {

    Department existedDepartment = this.departmentDAO.findDepartmentById(departmentLocationCreationDTO.departmentId())
      .orElseThrow(() -> new EntityNotFoundException(Department.class.getName()));

    DepartmentLocation departmentLocation = departmentLocationCreationDTO.toDepartmentLocation()
      .setDepartment(existedDepartment);

    return this.departmentDAO.saveDepartmentLocation(departmentLocation)
      .orElseThrow(InvalidRequestBodyException::new)
      .toDepartmentLocationDTO();
  }

  @Override
  @Transactional
  public DepartmentLocationDTO updatedDepartmentLocation(
    @NonNull Long id, @NonNull DepartmentLocationCreationDTO locationDTO
  ) throws EntityNotFoundException, InvalidRequestBodyException  {

    DepartmentLocation existedDepartmentLocation = this.departmentDAO.findDepartmentLocationById(id)
      .orElseThrow(() -> new EntityNotFoundException(DepartmentLocation.class.getName()));
    Department existedDepartment = this.departmentDAO.findDepartmentById(locationDTO.departmentId())
      .orElseThrow(() -> new EntityNotFoundException(Department.class.getName()));
    return this.departmentDAO.updateDepartmentLocation(
      locationDTO.toDepartmentLocation()
        .setId(id).setDepartment(existedDepartment)
    ).orElseThrow(InvalidRequestBodyException::new).toDepartmentLocationDTO();

  }


  @Override
  @Transactional
  public DepartmentDTO updateDepartment(
    @NonNull Long id, @NonNull DepartmentCreationDTO departmentCreationDTO
  ) throws EntityNotFoundException, JDBCException, InvalidRequestBodyException {

    Department existedDepartment = this.departmentDAO.findDepartmentById(id)
      .orElseThrow(() -> new EntityNotFoundException(Department.class.getName()));
    return this.departmentDAO.updateDepartment(
      departmentCreationDTO.toDepartment().setId(id)
    ).orElseThrow(InvalidRequestBodyException::new).toDepartmentDTO();
  }

  @Override
  @Transactional(value = Transactional.TxType.REQUIRED)
  public void deleteDepartmentLocation(Long id) throws EntityNotFoundException {
    this.departmentDAO.deleteDepartmentLocationById(id)
      .filter((rowsDeleted) -> rowsDeleted == 1)
      .orElseThrow(() -> new EntityNotFoundException(Department.class.getName()));
  }

  @Override
  public DepartmentDTO getDepartment(Long id) throws EntityNotFoundException {
    return this.departmentDAO.findDepartmentById(id)
      .orElseThrow(() -> new EntityNotFoundException(Department.class.getName()))
      .toDepartmentDTO();
  }
}
