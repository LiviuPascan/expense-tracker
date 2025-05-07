package com.springliviu.expensetracker.controller;

import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.Expense;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.service.ExpenseService;
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

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpensesForUser(@RequestParam Long userId) {
        // В реальном проекте будет использоваться авторизация вместо ручного userId
        User user = new User();
        user.setId(userId);

        List<Expense> expenses = expenseService.getExpensesByUser(user);
        return ResponseEntity.ok(expenses);
    }

    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody ExpenseRequest request) {
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

    public record ExpenseRequest(
            BigDecimal amount,
            String description,
            LocalDate date,
            Long userId,
            Long categoryId
    ) {
    }
}
