package com.springliviu.expensetracker.controller;

import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.Expense;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Operation(summary = "Получить все расходы текущего пользователя с фильтрами")
    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User user
    ) {
        var expenses = expenseService.getFilteredExpenses(
                user, from, to, categoryId, minAmount, maxAmount, sortBy, order, page, size
        );
        return ResponseEntity.ok(expenses.getContent());
    }

    @Operation(summary = "Создать новый расход")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Расход успешно создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Expense.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка запроса", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Expense> createExpense(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания расхода",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ExpenseRequest.class)))
            @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal User user
    ) {
        Category category = new Category();
        category.setId(request.categoryId());

        Expense created = expenseService.createExpense(
                request.amount(),
                request.description(),
                request.date(),
                user,
                category
        );

        return ResponseEntity.ok(created);
    }

    @Schema(description = "Запрос для создания расхода")
    public record ExpenseRequest(
            @Schema(description = "Сумма расхода", example = "1500.00")
            BigDecimal amount,

            @Schema(description = "Описание расхода", example = "Покупка продуктов")
            String description,

            @Schema(description = "Дата расхода", example = "2025-05-07")
            LocalDate date,

            @Schema(description = "ID категории", example = "3")
            Long categoryId
    ) {
    }
}
