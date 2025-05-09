package com.springliviu.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на создание категории")
public record CategoryRequest(
        @Schema(description = "Название категории", example = "Транспорт")
        String name
) {}
