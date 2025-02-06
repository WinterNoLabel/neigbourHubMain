package com.neighbourhub.community.event;

import com.neighbourhub.community.event.dto.CommunityEventDto;
import com.neighbourhub.community.event.dto.CreateCommunityEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommunityEventMapper {
    CommunityEvent toEntity(CommunityEventDto communityEventDto);

    CommunityEventDto toCommunityEventDto(CommunityEvent communityEvent);

    CommunityEvent toEntity(CreateCommunityEventDto createCommunityEventDto);

    CreateCommunityEventDto toCreateCommunityEventDto(CommunityEvent communityEvent);
}