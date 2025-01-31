package com.neighbourhub.community.utils;


import com.neighbourhub.community.dto.CommunityDTO;
import com.neighbourhub.community.entity.Community;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommunityMapper {
    CommunityDTO toDto(Community community);
    List<CommunityDTO> toDtoList(List<Community> communities);
}