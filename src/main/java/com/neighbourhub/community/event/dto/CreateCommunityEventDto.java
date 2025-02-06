package com.neighbourhub.community.event.dto;

import com.neighbourhub.community.event.CommunityEvent;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * DTO for {@link CommunityEvent}
 */
public record CreateCommunityEventDto(@NotNull @NotEmpty @NotBlank String name,
                                      @NotNull @NotEmpty @NotBlank String description,
                                      @NotNull @FutureOrPresent LocalDateTime eventDate) {

}