package com.neighbourhub.permissions.entity;

import lombok.Value;

import java.util.Set;

/**
 * DTO for {@link Role}
 */
@Value
public class RoleNamePermissionsDTO {
    Long id;
    String name;
    Set<Permission> permissions;
}