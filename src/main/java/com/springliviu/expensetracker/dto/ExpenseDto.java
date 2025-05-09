package com.springliviu.expensetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseDto(
        Long id,
        BigDecimal amount,
        String description,
        LocalDate date,
        Long categoryId,
        String categoryName
) {}
