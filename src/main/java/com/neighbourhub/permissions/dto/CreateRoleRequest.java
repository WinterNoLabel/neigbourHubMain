package com.neighbourhub.permissions.dto;

import com.neighbourhub.permissions.CommunityPermissionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class CreateRoleRequest {
    @NotBlank
    private String name;

    @NotNull
    private Long communityId;

    @NotEmpty
    private Set<CommunityPermissionType> permissions;
}
