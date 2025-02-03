package com.neighbourhub.location.community.entity;

import com.neighbourhub.location.LocationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

/**
 * DTO for {@link CommunityLocation}
 */
@Value
public class CommunityLocationCreateDto {
    @NotNull
    LocationType locationType;
    @NotNull
    @Positive
    Long locationId;
    @NotNull
    @Positive
    Long communityId;
}