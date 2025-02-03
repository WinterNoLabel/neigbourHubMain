package com.neighbourhub.community.service;

import com.neighbourhub.community.dto.CommunityCreateDTO;
import com.neighbourhub.community.dto.CommunityDTO;
import com.neighbourhub.community.dto.CommunitySearchCriteria;
import com.neighbourhub.community.entity.Community;
import com.neighbourhub.community.repository.CommunityRepository;
import com.neighbourhub.community.utils.CommunityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityMapper communityMapper;

    public CommunityService(CommunityRepository communityRepository,
                            @Autowired CommunityMapper communityMapper
    ) {
        this.communityRepository = communityRepository;
        this.communityMapper = communityMapper;
    }

    public CommunityDTO createCommunity(CommunityCreateDTO dto) {
        Community community = CommunityCreateDTO.toCommunity(dto);
        community.setCreatedAt(LocalDateTime.now());

        return communityMapper.toDto(communityRepository.save(community));
    }

    public List<CommunityDTO> searchCommunities(CommunitySearchCriteria criteria) {
        List<Community> communities = communityRepository.searchCommunities(criteria);
        return communityMapper.toDtoList(communities);
    }


    public Integer updateCommunity(CommunityDTO dto) {
        return communityRepository.updateCommunity(dto.getName(), dto.getDescription(), dto.getId());
    }
}