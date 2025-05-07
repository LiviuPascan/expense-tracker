package com.springliviu.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Запрос на создание расхода")
public record ExpenseRequest(

        @Schema(description = "Сумма расхода", example = "1500.00")
        BigDecimal amount,

        @Schema(description = "Описание расхода", example = "Покупка продуктов")
        String description,

        @Schema(description = "Дата расхода", example = "2025-05-07")
        LocalDate date,

        @Schema(description = "ID пользователя", example = "1")
        Long userId,

        @Schema(description = "ID категории", example = "3")
        Long categoryId
) {}
