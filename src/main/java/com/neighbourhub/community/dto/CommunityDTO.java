package com.neighbourhub.community.dto;

import com.neighbourhub.community.entity.Community;
import lombok.Data;

@Data
public class CommunityDTO {

    private Long id;
    private String name;
    private String description;
    private Long creatorId;

}