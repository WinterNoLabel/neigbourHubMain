package com.neighbourhub.permissions;

import com.neighbourhub.permissions.dto.AssignRoleRequest;
import com.neighbourhub.permissions.dto.CreateRoleRequest;
import com.neighbourhub.permissions.entity.Role;
import com.neighbourhub.permissions.service.CommunityPermissionService;
import com.neighbourhub.permissions.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/communities/{communityId}/roles")
@Tag(name = "Управление ролями", description = "API для работы с ролями в сообществе")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final CommunityPermissionService permissionService;

    // Создание роли (только админ)
    @CommunityPermission(
            permission = CommunityPermissionType.CREATE_ROLES,
            communityId = "#communityId",
            userId = "#userId"
    )
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
            permission = CommunityPermissionType.MANAGE_ROLES,
            communityId = "#communityId",
            userId = "#userId"
    )
    @PostMapping("/assign")
    public ResponseEntity<Void> assignRole(
            @PathVariable Long communityId,
            @RequestParam Long userId,
            @RequestBody @Valid AssignRoleRequest request
    ) {
        roleService.assignRoleToUser(
                request.getUserId(),
                request.getRoleId(),
                communityId
        );
        return ResponseEntity.ok().build();
    }

    @CommunityPermission(
            permission = CommunityPermissionType.MANAGE_ROLES,
            communityId = "#communityId",
            userId = "#userId"
    )
    @DeleteMapping("/revoke")
    public ResponseEntity<Void> revokeRole(
            @PathVariable Long communityId,
            @RequestParam Long userId,
            @RequestBody @Valid AssignRoleRequest request
    ) {
        roleService.revokeRoleFromUser(
                request.getUserId(),
                request.getRoleId(),
                communityId
        );
        return ResponseEntity.noContent().build();
    }

    @CommunityPermission(
            permission = CommunityPermissionType.DELETE_ROLES,
            communityId = "#communityId",
            userId = "#userId"
    )
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