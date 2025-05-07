package com.springliviu.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на создание или получение пользователя")
public record UserRequest(
        @Schema(description = "Имя пользователя", example = "karina123")
        String username,

        @Schema(description = "Пароль", example = "securePass123")
        String password
) {}
