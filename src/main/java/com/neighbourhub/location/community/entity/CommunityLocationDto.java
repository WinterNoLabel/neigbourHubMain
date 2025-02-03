package com.neighbourhub.location.community.entity;

import com.neighbourhub.location.LocationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO for {@link CommunityLocation}
 */
public record CommunityLocationDto(Long id, @NotNull LocationType locationType, @NotNull @Positive Long locationId,
                                   @NotNull @Positive Long communityId) {
}