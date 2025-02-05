package com.neighbourhub.permissions.service;

import com.neighbourhub.permissions.CommunityPermissionType;
import com.neighbourhub.permissions.entity.Role;
import com.neighbourhub.permissions.repository.UserCommunityRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityPermissionService {
    private final UserCommunityRoleRepository userCommunityRoleRepo;

    //    @Cacheable(value = "userPermissions", key = "{#userId, #communityId, #permission}")
    @Transactional
    public boolean hasPermission(Long userId, Long communityId, CommunityPermissionType permission) {
        List<Role> roles = userCommunityRoleRepo.findRolesByUserAndCommunity(userId, communityId);

        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(p -> p.getType() == permission);
    }
}