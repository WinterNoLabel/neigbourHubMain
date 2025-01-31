package com.neighbourhub.community.dto;

import com.neighbourhub.community.entity.Community;
import lombok.Data;

@Data
public class CommunityDTO {

    private Long id;
    private String name;
    private String description;
    private Long creatorId;

    public static CommunityDTO fromCommunity(Community community) {
        CommunityDTO dto = new CommunityDTO();
        dto.id = community.getId();
        dto.creatorId = community.getCreatorId();
        dto.description = community.getDescription();
        dto.name = community.getName();

        return dto;
    }
}