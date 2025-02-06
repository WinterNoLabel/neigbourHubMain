package com.neighbourhub.permissions.service;

import com.neighbourhub.permissions.CommunityPermissionType;
import com.neighbourhub.permissions.entity.Permission;
import com.neighbourhub.permissions.entity.Role;
import com.neighbourhub.permissions.repository.PermissionRepository;
import com.neighbourhub.permissions.repository.UserCommunityRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityPermissionService {
    private final UserCommunityRoleRepository userCommunityRoleRepo;
    private final PermissionRepository permissionRepo;

    //    @Cacheable(value = "userPermissions", key = "{#userId, #communityId, #permission}")
    @Transactional
    public boolean hasPermission(Long userId, Long communityId, CommunityPermissionType permission) {
        List<Role> roles = userCommunityRoleRepo.findRolesByUserAndCommunity(userId, communityId);

        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(p -> p.getType() == permission);
    }

    public List<Permission> getPermissions() {
        return permissionRepo.findAll();
    }

}