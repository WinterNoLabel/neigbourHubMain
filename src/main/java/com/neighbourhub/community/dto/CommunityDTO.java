package com.neighbourhub.community.dto;

import lombok.Data;

@Data
public class CommunityDTO {

    private Long id;
    private String name;
    private String description;
    private Long creatorId;
    private String photoUrl;

}