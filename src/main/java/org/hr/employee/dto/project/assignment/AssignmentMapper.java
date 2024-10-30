package org.hr.employee.dto.project.assignment;

import org.gateway.service.project.assignment.AssignmentPayloadProto;
import org.gateway.service.project.assignment.AssignmentResponseProto;
import org.hr.employee.dto.employee.EmployeeMapper;
import org.hr.employee.dto.project.ProjectMapper;
import org.hr.employee.entity.Assignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
  unmappedSourcePolicy = ReportingPolicy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  typeConversionPolicy = ReportingPolicy.WARN,
  uses = {
    EmployeeMapper.class,
    ProjectMapper.class
  }
)
public interface AssignmentMapper {

  static final AssignmentMapper INSTANCE = Mappers.getMapper(AssignmentMapper.class);

  @Mapping(source = "numberOfHours", target = "numberOfHours")
  Assignment assignmentPayloadDtoToAssignment(AssignmentPayloadDto assignmentPayloadDto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "numberOfHours", target = "numberOfHours")
  AssignmentPlainDto assignmentToAssignmentPlainDto(Assignment assignment);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "numberOfHours", target = "numberOfHours")
  @Mapping(source = "employeeAssigned", target = "employeePlainDto")
  @Mapping(source = "projectBelonging", target = "projectPlainDto")
  AssignmentResponseDto assignmentToAssignmentResponseDto(Assignment assignment);

  @Mapping(source = "numberOfHours", target = "numberOfHours")
  @Mapping(source = "employeePlainProto", target = "employeePlainDto")
  @Mapping(source = "projectPlainProto", target = "projectPlainDto")
  AssignmentPayloadDto assignmentPayloadProtoToAssignmentPayloadDto(AssignmentPayloadProto assignmentPayloadProto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "numberOfHours", target = "numberOfHours")
  @Mapping(source = "employeePlainDto", target = "employeePlainProto")
  @Mapping(source = "projectPlainDto", target = "projectPlainProto")
  AssignmentResponseProto assignmentResponseDtoToAssignmentResponseProto(AssignmentResponseDto assignmentResponseDto);
}
