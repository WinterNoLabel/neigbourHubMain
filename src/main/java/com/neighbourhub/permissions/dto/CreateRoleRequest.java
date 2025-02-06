package com.neighbourhub.permissions.dto;

import com.neighbourhub.permissions.CommunityPermissionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
@Schema(description = "Запрос на создание роли")
public class CreateRoleRequest {
    @Schema(
            description = "Название роли",
            example = "Модератор",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String name;

    @Schema(
            description = "Идентификаторы прав для роли",
            example = "[1, 2]",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotEmpty
    private Set<Long> permissions;
}
