package com.springliviu.expensetracker.controller;

import com.springliviu.expensetracker.dto.ExpenseDto;
import com.springliviu.expensetracker.dto.ExpensePageDto;
import com.springliviu.expensetracker.dto.ExpenseRequest;
import com.springliviu.expensetracker.dto.ExpenseSummaryDto;
import com.springliviu.expensetracker.mapper.ExpenseMapper;
import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.security.UserDetailsImpl;
import com.springliviu.expensetracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
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

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseMapper expenseMapper;

    public ExpenseController(ExpenseService expenseService, ExpenseMapper expenseMapper) {
        this.expenseService = expenseService;
        this.expenseMapper = expenseMapper;
    }

    @Operation(summary = "Фильтрация расходов с пагинацией и общей суммой (DTO)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Страница DTO расходов",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensePageDto.class))),
            @ApiResponse(responseCode = "400", description = "Неверные параметры запроса", content = @Content)
    })
    @GetMapping
    public ResponseEntity<ExpensePageDto> getExpenses(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ExpensePageDto dtoPage = expenseService.getFilteredExpensesDto(
                userDetails.getUser(),
                from, to,
                categoryId,
                minAmount, maxAmount,
                sortBy, order,
                page, size
        );
        return ResponseEntity.ok(dtoPage);
    }

    @Operation(summary = "Создать новый расход")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Расход успешно создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка запроса", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ExpenseDto> createExpense(
            @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        var category = new Category();
        category.setId(request.categoryId());

        var expense = expenseService.createExpense(
                request.amount(),
                request.description(),
                request.date(),
                userDetails.getUser(),
                category
        );
        return ResponseEntity.ok(expenseMapper.toDto(expense));
    }

    @Operation(summary = "Обновить расход по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Расход успешно обновлён",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseDto.class))),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса", content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет доступа к ресурсу", content = @Content),
            @ApiResponse(responseCode = "404", description = "Расход не найден", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDto> updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        var updated = expenseService.updateExpense(id, request, userDetails.getUser());
        return ResponseEntity.ok(expenseMapper.toDto(updated));
    }

    @Operation(summary = "Удалить расход по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Расход успешно удалён"),
            @ApiResponse(responseCode = "403", description = "Нет доступа к ресурсу", content = @Content),
            @ApiResponse(responseCode = "404", description = "Расход не найден", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        expenseService.deleteExpense(id, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить суммы расходов по категориям")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Сводка по категориям",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseSummaryDto.class)))
    })
    @GetMapping("/summary-by-category")
    public ResponseEntity<?> getSummaryByCategory(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        var summary = expenseService.getSummaryByCategory(userDetails.getUser());
        return ResponseEntity.ok(summary);
    }
}
