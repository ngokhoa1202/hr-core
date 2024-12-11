package org.hr.employee.dto.department.location;

import org.gateway.service.department.DepartmentResponseProto;
import org.hr.employee.dto.department.DepartmentMapper;
import org.hr.employee.entity.DepartmentLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
  unmappedSourcePolicy = ReportingPolicy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  typeConversionPolicy = ReportingPolicy.WARN,
  uses = {
    DepartmentMapper.class
  }
)
public interface DepartmentLocationMapper {

  DepartmentLocationMapper INSTANCE = Mappers.getMapper(DepartmentLocationMapper.class);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "location", target = "location")
  @Mapping(source = "department", target = "departmentPlainDto")
  DepartmentLocationResponseDTO locationToLocationResponseDto(DepartmentLocation location);

  @Mapping(source = "location", target = "location")
  @Mapping(source = "departmentPlainDto", target = "department")
  DepartmentLocation locationPayloadDtoToLocation(DepartmentLocationPayloadDto locationCreationDto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "location", target = "location")
  DepartmentLocationPlainDto locationToLocationPlainDto(DepartmentLocation location);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "location", target = "location")
  DepartmentResponseProto.DepartmentLocationPlainProto departmentLocationPlainDtoToDepartmentLocationPlainProto(
    DepartmentLocationPlainDto departmentLocationPlainDto
  );
}
