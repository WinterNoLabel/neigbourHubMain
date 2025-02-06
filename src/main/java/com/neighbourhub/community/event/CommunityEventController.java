package com.neighbourhub.community.event;

import com.neighbourhub.community.event.dto.CommunityEventDto;
import com.neighbourhub.community.event.dto.CreateCommunityEventDto;
import com.neighbourhub.permissions.CommunityPermission;
import com.neighbourhub.permissions.CommunityPermissionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community/{communityId}/events")
@Tag(name = "Управление событиями", description = "API для создания и управления событиями в сообществе")
@RequiredArgsConstructor
public class CommunityEventController {

    private final CommunityEventService eventService;
    private final CommunityEventMapper communityEventMapper;

    @Operation(
            summary = "Создать событие",
            description = "Доступно участникам сообщества. Возвращает созданное событие."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Событие создано",
                    content = @Content(schema = @Schema(implementation = CreateCommunityEventDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Сообщество не найдено")
    })
    @CommunityPermission(
            permission = CommunityPermissionType.MANAGE_EVENTS
    )
    @PostMapping
    public ResponseEntity<CommunityEvent> createEvent(
            @Parameter(description = "ID сообщества", example = "123") @PathVariable Long communityId,
            @Parameter(description = "ID создателя", example = "456") @RequestParam Long userId,
            @RequestBody @Valid CreateCommunityEventDto event
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.createEvent(communityId, userId, communityEventMapper.toEntity(event)));
    }

    @Operation(
            summary = "Удалить событие",
            description = "Доступно создателю или администратору сообщества."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Событие удалено"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Событие не найдено")
    })
    @CommunityPermission(
            permission = CommunityPermissionType.MANAGE_EVENTS
    )
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @Parameter(description = "ID события", example = "5") @PathVariable Long eventId,
            @Parameter(description = "ID сообщества", example = "123") @PathVariable Long communityId,
            @Parameter(description = "ID пользователя", example = "456") @RequestParam Long userId
    ) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Получить список событий",
            description = "Возвращает активные события сообщества."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список событий",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CommunityEventDto.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<CommunityEventDto>> getEvents(
            @Parameter(description = "ID сообщества", example = "123") @PathVariable Long communityId
    ) {
        List<CommunityEventDto> communityEventDtoList = eventService.getCommunityEvents(communityId)
                .stream()
                .map(communityEventMapper::toCommunityEventDto)
                .toList();

        return ResponseEntity.ok(communityEventDtoList);
    }
}