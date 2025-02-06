package com.neighbourhub.permissions.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Запрос на назначение/отзыв роли")
public class AssignRoleRequest {

    @Schema(
            description = "ID пользователя которому выдается роль",
            example = "789",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private Long targetUserId;

    @Schema(
            description = "ID роли",
            example = "5",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private Long roleId;
}
