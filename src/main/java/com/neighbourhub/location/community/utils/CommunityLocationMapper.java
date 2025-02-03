package com.neighbourhub.location.community.utils;

import com.neighbourhub.location.community.entity.CommunityLocation;
import com.neighbourhub.location.community.entity.CommunityLocationCreateDto;
import com.neighbourhub.location.community.entity.CommunityLocationDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommunityLocationMapper {
    CommunityLocation toEntity(CommunityLocationDto communityLocationDto);

    CommunityLocationDto toCommunityLocationDto(CommunityLocation communityLocation);

    CommunityLocation toEntity(CommunityLocationCreateDto communityLocationCreateDto);

    CommunityLocationCreateDto toCommunityLocationCreateDto(CommunityLocation communityLocation);
}