package com.neighbourhub.permissions.controller;

import com.neighbourhub.permissions.CommunityPermission;
import com.neighbourhub.permissions.CommunityPermissionType;
import com.neighbourhub.permissions.dto.AssignRoleRequest;
import com.neighbourhub.permissions.dto.CreateRoleRequest;
import com.neighbourhub.permissions.entity.Role;
import com.neighbourhub.permissions.service.CommunityPermissionService;
import com.neighbourhub.permissions.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community/{communityId}/roles")
@Tag(name = "Управление ролями", description = "API для работы с ролями в сообществе")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final CommunityPermissionService permissionService;

    @CommunityPermission(
            permission = CommunityPermissionType.CREATE_ROLES
    )
    @Operation(
            summary = "Создать новую роль",
            description = "Доступно только администраторам сообщества. Роль привязывается к указанному сообществу."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Роль успешно создана",
                    content = @Content(schema = @Schema(implementation = Role.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав для выполнения операции"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сообщество не найдено"
            )
    })
    @PostMapping
    public ResponseEntity<Role> createRole(
            @PathVariable Long communityId,
            @RequestParam Long userId,
            @RequestBody @Valid CreateRoleRequest request
    ) {
        Role role = roleService.createRole(request.getName(), communityId, request.getPermissions());
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @CommunityPermission(
            permission = CommunityPermissionType.MANAGE_ROLES
    )
    @Operation(
            summary = "Назначить роль пользователю",
            description = "Доступно только администраторам сообщества."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Роль успешно назначена"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Роль/пользователь не найден"),
            @ApiResponse(responseCode = "409", description = "Роль уже назначена пользователю")
    })
    @PostMapping("/assign")
    public ResponseEntity<Void> assignRole(
            @PathVariable Long communityId,
            @RequestParam Long userId,
            @RequestBody @Valid AssignRoleRequest request
    ) {
        roleService.assignRoleToUser(
                request.getTargetUserId(),
                request.getRoleId(),
                communityId
        );
        return ResponseEntity.ok().build();
    }

    @CommunityPermission(
            permission = CommunityPermissionType.MANAGE_ROLES
    )
    @Operation(
            summary = "Отозвать роль у пользователя",
            description = "Доступно только администраторам сообщества."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Роль успешно отозвана"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Связь не найдена")
    })
    @PostMapping("/revoke")
    public ResponseEntity<Void> revokeRole(
            @PathVariable Long communityId,
            @RequestParam Long userId,
            @RequestBody @Valid AssignRoleRequest request
    ) {
        roleService.revokeRoleFromUser(
                request.getTargetUserId(),
                request.getRoleId(),
                communityId
        );
        return ResponseEntity.noContent().build();
    }

    @CommunityPermission(
            permission = CommunityPermissionType.MANAGE_ROLES
    )
    @Operation(
            summary = "Удалить роль",
            description = "Удаляет роль и все связанные назначения. Доступно только администраторам."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Роль удалена"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Роль не найдена")
    })
    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(
            @PathVariable Long communityId,
            @PathVariable Long roleId,
            @RequestParam Long userId
    ) {
        roleService.deleteRole(roleId, communityId);
        return ResponseEntity.noContent().build();
    }
}