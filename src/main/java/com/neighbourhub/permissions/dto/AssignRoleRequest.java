package com.neighbourhub.permissions.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignRoleRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long roleId;

    @NotNull
    private Long communityId;
}
