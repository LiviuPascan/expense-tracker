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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Operation(summary = "Получить все расходы пользователя")
    @ApiResponse(responseCode = "200", description = "Список расходов успешно получен",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Expense.class)))
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpensesForUser(
            @Parameter(description = "ID пользователя", example = "1")
            @RequestParam Long userId) {
        User user = new User();
        user.setId(userId);

        List<Expense> expenses = expenseService.getExpensesByUser(user);
        return ResponseEntity.ok(expenses);
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
            @RequestBody ExpenseRequest request) {

        User user = new User();
        user.setId(request.userId());

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

            @Schema(description = "ID пользователя", example = "1")
            Long userId,

            @Schema(description = "ID категории", example = "3")
            Long categoryId
    ) {
    }
}
