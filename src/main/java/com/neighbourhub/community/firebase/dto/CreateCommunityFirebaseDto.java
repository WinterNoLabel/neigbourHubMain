package com.neighbourhub.community.firebase.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class CreateCommunityFirebaseDto {

    Long communityId;
    Long creatorId;
    String name;
    Instant createdAt;

}
