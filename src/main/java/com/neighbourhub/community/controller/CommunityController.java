package com.neighbourhub.community.controller;

import com.neighbourhub.community.dto.CommunityCreateDTO;
import com.neighbourhub.community.dto.CommunityDTO;
import com.neighbourhub.community.dto.CommunitySearchCriteria;
import com.neighbourhub.community.service.CommunityMinioService;
import com.neighbourhub.community.service.CommunityService;
import com.neighbourhub.permissions.CommunityPermission;
import com.neighbourhub.permissions.CommunityPermissionType;
import com.neighbourhub.utils.web.dto.ValidationErrorResponse;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/community")
@Tag(name = "Сообщества", description = "API для управления сообществами")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;
    private final CommunityMinioService communityMinioService;

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
            description = "Поиск сообществ по различным параметрам"
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CommunityDTO.class))
            )
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

    @PatchMapping("/{communityId}")
    @Operation(
            summary = "Обновление описания",
            description = "Обновление описания сообщества"
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
    @CommunityPermission(
            permission = CommunityPermissionType.EDIT_DESCRIPTION
    )
    public ResponseEntity<?> updateDescription(@PathVariable Long communityId,
                                               @RequestParam Long userId,
                                               @RequestBody String description) {
        Integer updatedCommunityId = communityService.updateDescription(description, communityId);
        return ResponseEntity.ok(updatedCommunityId);
    }

    @Operation(
            summary = "Загрузить фотографию сообщества",
            description = "Доступно администраторам сообщества. Возвращает обновленный URL фото."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фото обновлено"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Сообщество не найдено"),
            @ApiResponse(responseCode = "400", description = "Ошибка загрузки файла")
    })
    @CommunityPermission(
            permission = CommunityPermissionType.EDIT_PHOTO
    )
    @PostMapping(value = "/{communityId}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityDTO> uploadPhoto(
            @Parameter(description = "ID сообщества", example = "1")
            @PathVariable Long communityId,

            @Parameter(description = "ID администратора", example = "101")
            @RequestParam Long userId,

            @Parameter(description = "Файл изображения (JPEG/PNG, до 5MB)")
            @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Файл не выбран");
        }
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Только изображения (JPEG, PNG)");
        }

        try {
            String photoUrl = communityMinioService.uploadFile(file, "community-" + communityId);

            CommunityDTO updatedCommunity = communityService.updatePhoto(communityId, photoUrl);
            return ResponseEntity.ok(updatedCommunity);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки файла: " + e.getMessage());
        }
}
}
