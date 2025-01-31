package com.neighbourhub.community.dto;

import lombok.Data;

@Data
public class CommunitySearchCriteria {
    private Long id;
    private String name;
    private String description;
    private Long creatorId;
}