package com.springliviu.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Запрос на создание или обновление расхода")
public record ExpenseRequest(

        @Schema(description = "Сумма расхода", example = "1500.00")
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        BigDecimal amount,

        @Schema(description = "Описание расхода", example = "Покупка продуктов")
        @NotBlank(message = "Description is required")
        @Size(max = 255, message = "Description must be under 255 characters")
        String description,

        @Schema(description = "Дата расхода", example = "2025-05-07")
        @NotNull(message = "Date is required")
        LocalDate date,

        @Schema(description = "ID категории", example = "3")
        @NotNull(message = "Category ID is required")
        Long categoryId
) {}
