package org.hr.employee.dto.employee;

import jakarta.persistence.Tuple;
import org.gateway.service.project.assignment.AssignmentPayloadProto;
import org.hr.employee.dto.department.DepartmentMapper;
import org.hr.employee.dto.department.DepartmentPlainDto;
import org.hr.employee.entity.Employee;
import org.hr.employee.entity.GenderEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Locale;

@Mapper(
  unmappedSourcePolicy = ReportingPolicy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  typeConversionPolicy = ReportingPolicy.WARN,
  uses = {
    DepartmentMapper.class
  }
)
public interface EmployeeMapper {

  EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

  @Mapping(source = "employeeId", target = "employeeId")
  @Mapping(source = "firstname", target = "firstname")
  @Mapping(source = "lastname", target = "lastname")
  @Mapping(source = "middlename", target = "middlename")
  @Mapping(source = "gender", target = "gender", qualifiedByName = "toGenderEnum")
  @Mapping(source = "salary", target = "salary")
  @Mapping(source = "dateOfBirth", target = "dateOfBirth")
  @Mapping(source = "departmentPlainDto", target = "department")
  Employee employeePayloadDtoToEmployee(EmployeePayloadDto employeePayloadDto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "firstname", target = "firstname")
  @Mapping(source = "lastname", target = "lastname")
  @Mapping(source = "middlename", target = "middlename")
  @Mapping(source = "gender", target = "gender", qualifiedByName = "toGenderString")
  @Mapping(source = "salary", target = "salary")
  @Mapping(source = "dateOfBirth", target = "dateOfBirth")
  @Mapping(source = "department", target = "departmentPlainDto")
  EmployeeResponseDto employeeToEmployeeResponseDto(Employee employee);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "employeeId", target = "employeeId")
  @Mapping(source = "firstname", target = "firstname")
  @Mapping(source = "lastname", target = "lastname")
  @Mapping(source = "middlename", target = "middlename")
  @Mapping(source = "gender", target = "gender", qualifiedByName = "toGenderEnum")
  @Mapping(source = "salary", target = "salary")
  @Mapping(source = "dateOfBirth", target = "dateOfBirth")
  Employee employeePlainDtoToEmployee(EmployeePlainDto employeePlainDto);

  @Named("toGenderEnum")
  default GenderEnum toGenderEnum(String gender) {
    return GenderEnum.valueOf(gender.toUpperCase(Locale.ROOT));
  }

  @Named("toGenderString")
  default String toGenderString(GenderEnum genderEnum) {
    return genderEnum.toString().toUpperCase(Locale.ROOT);
  }

  @Mapping(expression = "java(tuple.get(\"employeeUUID\", UUID.class))", target = "id")
  @Mapping(expression = "java(tuple.get(\"employeeId\", String.class))", target = "employeeId")
  @Mapping(expression = "java(tuple.get(\"firstname\", String.class))", target = "firstname")
  @Mapping(expression = "java(tuple.get(\"lastname\", String.class))", target = "lastname")
  @Mapping(expression = "java(tuple.get(\"middlename\", String.class))", target = "middlename")
  @Mapping(expression = "java(tuple.get(\"dateOfBirth\", LocalDateTime.class))", target = "dateOfBirth")
  @Mapping(expression = "java(tuple.get(\"salary\", Integer.class))", target = "salary")
  @Mapping(expression = "java(tuple.get(\"gender\", GenderEnum.class))", target = "gender")
  @Mapping(source = "tuple", target = "departmentPlainDto")
  @Mapping(expression = "java(tuple.get(\"hoursSpentPerAssignment\", Double.class))", target = "hoursSpentPerAssignment")
  @Mapping(expression = "java(tuple.get(\"totalHours\", Long.class))", target = "totalHours")
  @Mapping(expression = "java(tuple.get(\"numberOfAssignments\", Long.class))", target = "numberOfAssignments")
  EmployeeDepartmentAssignmentStatisticsDto tupleToEmployeeDepartmentAssignmentStatisticsDto(Tuple tuple);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "employeeId", target = "employeeId")
  @Mapping(source = "firstname", target = "firstname")
  @Mapping(source = "lastname", target = "lastname")
  @Mapping(source = "middlename", target = "middlename")
  @Mapping(source = "gender", target = "gender", qualifiedByName = "toGenderEnum")
  @Mapping(source = "salary", target = "salary")
  @Mapping(source = "dateOfBirth", target = "dateOfBirth")
  EmployeePlainDto employeePlainProtoToEmployeePlainDto(AssignmentPayloadProto.EmployeePlainProto employeePlainProto);
}
