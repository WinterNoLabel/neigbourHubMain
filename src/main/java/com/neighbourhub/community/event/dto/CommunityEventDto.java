package com.neighbourhub.community.event.dto;

import com.neighbourhub.community.event.CommunityEvent;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO for {@link CommunityEvent}
 */
@Value
public class CommunityEventDto {
    Long id;
    CommunityDto community;
    Long creatorUserId;
    String name;
    String description;
    LocalDateTime eventDate;
    LocalDateTime createdDate;
    LocalDateTime modifiedDate;
    LocalDateTime deletedDate;

    /**
     * DTO for {@link com.neighbourhub.community.entity.Community}
     */
    @Value
    public static class CommunityDto {
        Long id;
    }
}