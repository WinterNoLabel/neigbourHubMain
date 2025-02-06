package com.neighbourhub.permissions.service;

import com.neighbourhub.community.entity.Community;
import com.neighbourhub.community.repository.CommunityRepository;
import com.neighbourhub.permissions.CommunityPermissionType;
import com.neighbourhub.permissions.entity.Permission;
import com.neighbourhub.permissions.entity.Role;
import com.neighbourhub.permissions.entity.UserCommunityRole;
import com.neighbourhub.permissions.entity.UserCommunityRoleId;
import com.neighbourhub.permissions.repository.PermissionRepository;
import com.neighbourhub.permissions.repository.RoleRepository;
import com.neighbourhub.permissions.repository.UserCommunityRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserCommunityRoleRepository userCommunityRoleRepo;
    private final CommunityRepository communityRepository;

    public Role createRole(String name, Community community, Set<Permission> permissions) {
        Role role = new Role();
        role.setName(name);
        role.setCommunity(community);
        role.setPermissions(permissions);
        return roleRepository.save(role);
    }

    public void assignRoleToUser(Long targetUserId, Long roleId, Long communityId) {
        Role role = roleRepository.findByIdAndCommunity_Id(roleId, communityId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        Community community = communityRepository.findById(communityId).orElseThrow(
                () -> new EntityNotFoundException("Community not found"));

        UserCommunityRoleId id = new UserCommunityRoleId();
        id.setUserId(targetUserId);
        id.setCommunityId(communityId);
        id.setRoleId(roleId);

        if (userCommunityRoleRepo.existsById(id)) {
            return;
        }

        UserCommunityRole userRole = new UserCommunityRole();
        userRole.setId(id);
        userRole.setRole(role);
        userRole.setCommunity(community);
        userCommunityRoleRepo.save(userRole);
    }

    public void revokeRoleFromUser(Long targetUserId, Long roleId, Long communityId) {
        UserCommunityRoleId id = new UserCommunityRoleId();
        id.setUserId(targetUserId);
        id.setCommunityId(communityId);
        id.setRoleId(roleId);

        userCommunityRoleRepo.deleteById(id);
    }

    public void deleteRole(Long roleId, Long communityId) {
        Role role = roleRepository.findByIdAndCommunity_Id(roleId, communityId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        userCommunityRoleRepo.deleteAllByRoleId(roleId);
        roleRepository.delete(role);
    }

    public Role createRole(String name, Long communityId, List<CommunityPermissionType> permissions) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));

        Set<Permission> permissionEntities = permissions.stream()
                .map(p -> permissionRepository.findByType(p)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid permission: " + p))
                )
                .collect(Collectors.toSet());

        Role role = new Role();
        role.setName(name);
        role.setCommunity(community);
        role.setPermissions(permissionEntities);
        return roleRepository.save(role);
    }

    public Role createRole(String name, Long communityId, Set<Long> permissionsIds) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));

        Set<Permission> permissionEntities = permissionsIds.stream()
                .map(p -> permissionRepository.findById(p)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid permission: " + p))
                )
                .collect(Collectors.toSet());

        Role role = new Role();
        role.setName(name);
        role.setCommunity(community);
        role.setPermissions(permissionEntities);
        return roleRepository.save(role);
    }

    public List<Role> getUserRolesInCommunity(Long communityId, Long memberId) {
        return userCommunityRoleRepo.findRolesByUserAndCommunity(memberId, communityId);
    }
}