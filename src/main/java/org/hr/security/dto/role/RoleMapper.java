package org.hr.security.dto.role;

import org.hr.security.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {

  static final RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  RoleResponseDto roleToRoleResponseDto(Role role);

  @Mapping(source = "name", target = "name")
  Role rolePayloadDtoToRole(RolePayloadDto rolePayloadDto);
}
