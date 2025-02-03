package com.neighbourhub.community.controller;

import com.neighbourhub.community.dto.CommunityCreateDTO;
import com.neighbourhub.community.dto.CommunityDTO;
import com.neighbourhub.community.dto.CommunitySearchCriteria;
import com.neighbourhub.community.service.CommunityService;
import com.neighbourhub.utils.web.dto.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community")
@Tag(name = "Сообщества", description = "API для управления сообществами")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @PostMapping
    @Operation(
            summary = "Создать сообщество",
            description = "Создает новое сообщество с указанными данными"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Сообщество успешно создано",
            content = @Content(schema = @Schema(implementation = CommunityDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))
    )
    public ResponseEntity<?> createCommunity(
            @RequestBody @Valid CommunityCreateDTO dto
    )
    {
        CommunityDTO createdDto = communityService.createCommunity(dto);

        return ResponseEntity.ok(createdDto);
    }

    @GetMapping
    @Operation(
            summary = "Поиск сообществ",
            description = "Поиск сообщества по различным параметрам"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Сообщество успешно создано",
            content = @Content(schema = @Schema(implementation = CommunityDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))
    )
    public ResponseEntity<List<CommunityDTO>> searchCommunities(
            @Parameter(description = "ID сообщества") @RequestParam(required = false) Long id,
            @Parameter(description = "Название (частичное совпадение)") @RequestParam(required = false) String name,
            @Parameter(description = "Описание (частичное совпадение)") @RequestParam(required = false) String description,
            @Parameter(description = "ID создателя") @RequestParam(required = false) Long creatorId
    ) {
        CommunitySearchCriteria criteria = new CommunitySearchCriteria();
        criteria.setId(id);
        criteria.setName(name);
        criteria.setDescription(description);
        criteria.setCreatorId(creatorId);

        List<CommunityDTO> result = communityService.searchCommunities(criteria);
        return ResponseEntity.ok(result);
    }

    @PatchMapping
    @Operation(
            summary = "Обновление сообщества",
            description = "Обновление параметров сообщества"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Идентификатор успешно отредактированого сообщества",
            content = @Content(schema = @Schema(implementation = Integer.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))
    )
    public ResponseEntity<?> updateCommunity(@RequestBody CommunityDTO dto) {
        Integer updatedCommunityId = communityService.updateCommunity(dto);
        return ResponseEntity.ok(updatedCommunityId);
    }

}
