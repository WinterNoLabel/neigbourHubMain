package com.neighbourhub.permissions;

import com.neighbourhub.permissions.entity.Role;
import com.neighbourhub.permissions.entity.RoleNamePermissionsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    Role toEntity(RoleNamePermissionsDTO roleNamePermissionsDTO);

    RoleNamePermissionsDTO toRoleNamePermissionsDTO(Role role);
}