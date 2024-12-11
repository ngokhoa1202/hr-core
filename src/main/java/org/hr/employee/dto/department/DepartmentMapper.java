package org.hr.employee.dto.department;

import jakarta.persistence.Tuple;
import org.gateway.service.department.DepartmentPayloadProto;
import org.gateway.service.department.DepartmentPlainProto;
import org.gateway.service.department.DepartmentResponseProto;
import org.hr.employee.dto.department.location.DepartmentLocationMapper;
import org.hr.employee.dto.department.location.DepartmentLocationPlainDto;
import org.hr.employee.entity.Department;
import org.hr.employee.entity.DepartmentLocation;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(
  unmappedSourcePolicy = ReportingPolicy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  typeConversionPolicy = ReportingPolicy.WARN,
  uses = {
    DepartmentLocationMapper.class
  }
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

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "startDate", target = "startDate")
  DepartmentResponseProto departmentResponseDtoToDepartmentResponseProto(DepartmentResponseDto departmentResponseDto);

  @AfterMapping
  default DepartmentResponseProto mapDepartmentLocationPlainDtosToDepartmentLocationPlainProtos(
    DepartmentResponseDto source, @MappingTarget DepartmentResponseProto target
  ) {

    return target.toBuilder().addAllLocationProtos(
      source.locationPlainDtos()
        .stream()
        .map(DepartmentLocationMapper.INSTANCE::departmentLocationPlainDtoToDepartmentLocationPlainProto)
        .toList()
    ).build();
  }

  @Mapping(source = "name", target = "name")
  @Mapping(source = "startDate", target = "startDate")
  DepartmentPayloadDto departmentPlainProtoToDepartmentPayloadDto(DepartmentPlainProto departmentPlainProto);

  @Mapping(source = "name", target = "name")
  @Mapping(source = "startDate", target = "startDate")
  DepartmentPayloadDto departmentPayloadProtoToDepartmentPayloadDto(DepartmentPayloadProto departmentPayloadProto);

}
