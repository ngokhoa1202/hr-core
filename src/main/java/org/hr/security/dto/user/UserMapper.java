package org.hr.security.dto.user;

import org.hr.security.dto.role.RoleMapper;
import org.hr.security.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
  unmappedSourcePolicy = ReportingPolicy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  typeConversionPolicy = ReportingPolicy.WARN,
  uses = {
    RoleMapper.class
  }
)
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "role", target = "rolePlainDto")
  UserResponseDto userToUserResponseDto(User user);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "rolePlainDto", target = "role")
  User userPayloadDtoToUser(UserPayloadDto userPayloadDto);
}
