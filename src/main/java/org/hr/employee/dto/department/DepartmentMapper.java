package org.hr.employee.dto.department;

import jakarta.persistence.Tuple;
import org.hr.employee.dto.department.location.DepartmentLocationMapper;
import org.hr.employee.dto.department.location.DepartmentLocationPlainDto;
import org.hr.employee.entity.Department;
import org.hr.employee.entity.DepartmentLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(
  unmappedSourcePolicy = ReportingPolicy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  typeConversionPolicy = ReportingPolicy.WARN
)
public interface DepartmentMapper {

  DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "startDate", target = "startDate")
  @Mapping(source = "locations", target = "locationPlainDtos", qualifiedByName = "toLocationPlainDtos")
  DepartmentResponseDto departmentToDepartmentResponseDto(Department department);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "startDate", target = "startDate")
  DepartmentPlainDto departmentToDepartmentPlainDto(Department department);

  @Mapping(source = "name", target = "name")
  @Mapping(source = "startDate", target = "startDate")
  Department departmentPayloadDtoToDepartment(DepartmentPayloadDto departmentPayloadDto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "startDate", target = "startDate")
  Department departmentPlainDtoToDepartment(DepartmentPlainDto departmentPlainDto);

  @Mapping(expression = "java(tuple.get(\"departmentId\", Long.class))", target = "id")
  @Mapping(expression = "java(tuple.get(\"departmentName\", String.class))", target = "name")
  @Mapping(expression = "java(tuple.get(\"departmentStartDate\", LocalDateTime.class))", target = "startDate")
  DepartmentPlainDto tupleToDepartmentPlainDto(Tuple tuple);

  @Named("toLocationPlainDtos")
  default List<DepartmentLocationPlainDto> toLocationNameList(Set<DepartmentLocation> locations) {
    return locations.stream()
      .map(DepartmentLocationMapper.INSTANCE::locationToLocationPlainDto)
      .toList();
  }



}
