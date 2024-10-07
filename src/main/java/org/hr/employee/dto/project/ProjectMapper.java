package org.hr.employee.dto.project;

import jakarta.persistence.Tuple;
import org.hr.employee.dto.department.DepartmentMapper;
import org.hr.employee.dto.project.assignment.AssignmentMapper;
import org.hr.employee.dto.project.assignment.AssignmentPlainDto;
import org.hr.employee.entity.Assignment;
import org.hr.employee.entity.Project;
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
  typeConversionPolicy = ReportingPolicy.WARN,
  uses = {
    DepartmentMapper.class
  }
)
public interface ProjectMapper {

  ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

  @Mapping(source = "name", target = "name")
  @Mapping(source = "area", target = "area")
  @Mapping(source = "departmentPlainDto", target = "managedDepartment")
  Project projectPayloadDtoToProject(ProjectPayloadDto projectPayloadDto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "area", target = "area")
  @Mapping(source = "assignments", target = "assignmentPlainDtos", qualifiedByName = "toAssignmentPlainDtos")
  ProjectResponseDto projectToProjectResponseDto(Project project);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "area", target = "area")
  Project projectPlainDtoToProject(ProjectPlainDto projectPlainDto);

  @Mapping(expression = "java(tuple.get(\"projectId\", Long.class))", target = "id")
  @Mapping(expression = "java(tuple.get(\"projectName\", String.class))", target = "name")
  @Mapping(expression = "java(tuple.get(\"projectArea\", String.class))", target = "area")
  @Mapping(source = "tuple", target = "departmentPlainDto")
  @Mapping(expression = "java(tuple.get(\"numberOfAssignments\", Long.class))", target = "numberOfAssignments")
  @Mapping(expression = "java(tuple.get(\"totalHours\", Long.class))", target = "totalHours")
  @Mapping(expression = "java(tuple.get(\"hoursSpentPerAssignment\", Double.class))", target = "hoursSpentPerAssignment")
  ProjectAssignmentStatisticsDto tupleToProjectAssignmentStatisticsDto(Tuple tuple);

  @Named(value = "toAssignmentPlainDtos")
  default List<AssignmentPlainDto> toAssignmentPlainDtos(Set<Assignment> assignments) {
    return assignments.stream()
      .map(AssignmentMapper.INSTANCE::assignmentToAssignmentPlainDto)
      .toList();
  }
}
