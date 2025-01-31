package com.neighbourhub.utils.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Ошибка валидации")
public record ValidationErrorResponse(
        @Schema(description = "Код ошибки", example = "VALIDATION_FAILED")
        String code,

        @Schema(description = "Детали ошибок")
        Map<String, String> errors
) {}
