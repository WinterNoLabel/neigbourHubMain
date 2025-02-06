package com.neighbourhub.member;

import com.neighbourhub.member.dto.CommunityMemberWithRoles;
import com.neighbourhub.permissions.CommunityPermission;
import com.neighbourhub.permissions.CommunityPermissionType;
import com.neighbourhub.permissions.RoleMapper;
import com.neighbourhub.permissions.entity.RoleNamePermissionsDTO;
import com.neighbourhub.permissions.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/community/{communityId}/members")
@Tag(name = "Управление членами сообщества", description = "API для добавления и удаления участников сообщества")
@RequiredArgsConstructor
public class CommunityMemberController {

    private final CommunityMemberService communityMemberService;
    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @Operation(
            summary = "Добавить участника",
            description = "Добавляет пользователя в сообщество. Доступно только администраторам."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Участник успешно добавлен"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Сообщество не найдено"),
    })
    @CommunityPermission(
            permission = CommunityPermissionType.MANAGE_MEMBERS,
            communityId = "#communityId",
            userId = "#userId"
    )
    @PostMapping
    public ResponseEntity<Void> addMember(
            @Parameter(description = "ID сообщества", example = "123")
            @PathVariable Long communityId,

            @Parameter(description = "ID администратора", example = "456")
            @RequestParam Long userId,

            @Parameter(description = "ID добавляемого пользователя", example = "789")
            @RequestParam Long targetUserId
    ) {
        communityMemberService.addMember(communityId, targetUserId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Удалить участника",
            description = "Удаляет пользователя из сообщества. Доступно только администраторам."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Участник успешно удален"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Пользователь не является участником")
    })
    @CommunityPermission(
            permission = CommunityPermissionType.MANAGE_MEMBERS,
            communityId = "#communityId",
            userId = "#userId"
    )
    @DeleteMapping
    public ResponseEntity<Void> removeMember(
            @Parameter(description = "ID сообщества", example = "123")
            @PathVariable Long communityId,

            @Parameter(description = "ID администратора", example = "456")
            @RequestParam Long userId,

            @Parameter(description = "ID удаляемого пользователя", example = "789")
            @RequestParam Long targetUserId
    ) {
        communityMemberService.removeMember(communityId, targetUserId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Получить список участников",
            description = "Возвращает список ID участников сообщества."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список участников",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CommunityMemberWithRoles.class))
                    )
            ),

            @ApiResponse(responseCode = "404", description = "Сообщество не найдено")
    })
    @GetMapping
    public ResponseEntity<List<CommunityMemberWithRoles>> getMembers(
            @Parameter(description = "ID сообщества", example = "123")
            @PathVariable Long communityId
    ) {
        List<CommunityMemberWithRoles> members = new ArrayList<>();
        communityMemberService.getMembers(communityId)
                .forEach(memberId -> {
                    CommunityMemberWithRoles member = new CommunityMemberWithRoles();
                    List<RoleNamePermissionsDTO> rolesInCommunity = roleService
                            .getUserRolesInCommunity(communityId, memberId)
                            .stream()
                            .map(roleMapper::toRoleNamePermissionsDTO).toList();
                    member.setId(memberId);
                    member.setRoles(rolesInCommunity);
                    members.add(member);
                });
        return ResponseEntity.ok(members);
    }
}