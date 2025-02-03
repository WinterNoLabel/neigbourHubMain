package com.neighbourhub.location.community.controller;

import com.neighbourhub.location.community.entity.CommunityLocation;
import com.neighbourhub.location.community.entity.CommunityLocationCreateDto;
import com.neighbourhub.location.community.entity.CommunityLocationDto;
import com.neighbourhub.location.community.service.CommunityLocationService;
import com.neighbourhub.location.community.utils.CommunityLocationMapper;
import com.neighbourhub.utils.web.dto.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community-location")
@RequiredArgsConstructor
public class CommunityLocationController {

    private final CommunityLocationService communityLocationService;
    private final CommunityLocationMapper communityLocationMapper;


    @PostMapping
    @Operation(
            summary = "Привязать сообщество к местоположению",
            description = "Добавляет к определенному местоположению сообщество"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Успешная привязка",
            content = @Content(schema = @Schema(implementation = CommunityLocationDto.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))
    )
    public CommunityLocationDto create(@RequestBody @Valid CommunityLocationCreateDto dto) {
        CommunityLocation createdLocation = communityLocationService.create(communityLocationMapper.toEntity(dto));

        return communityLocationMapper.toCommunityLocationDto(createdLocation);
    }

    @GetMapping("/{communityId}")
    @Operation(
            summary = "Поиск местоположения сообщества по ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Местоположение сообщества",
            content = @Content(schema = @Schema(implementation = CommunityLocationDto.class))
    )
    public ResponseEntity<CommunityLocationDto> getCommunityLocation(@PathVariable long communityId) {
        try {
            CommunityLocation communityLocation = communityLocationService.getCommunityLocation(communityId);

            return ResponseEntity.ok(communityLocationMapper.toCommunityLocationDto(communityLocation));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}