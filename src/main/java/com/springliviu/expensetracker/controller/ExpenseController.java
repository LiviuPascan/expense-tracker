package com.springliviu.expensetracker.controller;

import com.springliviu.expensetracker.security.UserDetailsImpl;
import com.springliviu.expensetracker.service.ExpenseService;
import com.springliviu.expensetracker.service.ExpenseService.ExpensePageDto;
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

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
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
                            schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка запроса", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> createExpense(
            @RequestBody com.springliviu.expensetracker.dto.ExpenseRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // мапим DTO → Category и передаем дальше
        var category = new com.springliviu.expensetracker.model.Category();
        category.setId(request.categoryId());

        expenseService.createExpense(
                request.amount(),
                request.description(),
                request.date(),
                userDetails.getUser(),
                category
        );
        return ResponseEntity.ok().build();
    }
}
