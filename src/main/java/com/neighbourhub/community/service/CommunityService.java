package com.neighbourhub.community.service;

import com.neighbourhub.community.dto.CommunityCreateDTO;
import com.neighbourhub.community.dto.CommunityDTO;
import com.neighbourhub.community.dto.CommunitySearchCriteria;
import com.neighbourhub.community.entity.Community;
import com.neighbourhub.community.repository.CommunityRepository;
import com.neighbourhub.community.utils.CommunityMapper;
import com.neighbourhub.permissions.CommunityPermissionType;
import com.neighbourhub.permissions.entity.Role;
import com.neighbourhub.permissions.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityMapper communityMapper;
    private final RoleService roleService;

    public CommunityDTO createCommunity(CommunityCreateDTO dto) {
        Community community = CommunityCreateDTO.toCommunity(dto);
        community.setCreatedAt(LocalDateTime.now());

        Community createdCommunity = communityRepository.save(community);

        Role creatorRole = roleService.createRole("Создатель", createdCommunity.getId(), CommunityPermissionType.allPerms());

        roleService.assignRoleToUser(dto.getCreatorId(), creatorRole.getId(), createdCommunity.getId());

        return communityMapper.toDto(createdCommunity);
    }

    public List<CommunityDTO> searchCommunities(CommunitySearchCriteria criteria) {
        List<Community> communities = communityRepository.searchCommunities(criteria);
        return communityMapper.toDtoList(communities);
    }

    public Integer updateCommunity(CommunityDTO dto) {
        return communityRepository.updateCommunity(dto.getName(), dto.getDescription(), dto.getId());
    }

    public Integer updateDescription(String description, Long id) {
        return communityRepository.updateDescription(description, id);
    }

    public CommunityDTO findById(Long id) {
        return communityMapper.toDto(communityRepository.findById(id).orElse(null));
    }
}