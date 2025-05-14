package com.springliviu.expensetracker.service;

import com.springliviu.expensetracker.dto.ExpenseDto;
import com.springliviu.expensetracker.dto.ExpensePageDto;
import com.springliviu.expensetracker.dto.ExpenseRequest;
import com.springliviu.expensetracker.dto.ExpenseSummaryDto;
import com.springliviu.expensetracker.mapper.ExpenseMapper;
import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.Expense;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.repository.ExpenseRepository;
import com.springliviu.expensetracker.repository.specification.ExpenseSpecification;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    public ExpenseService(ExpenseRepository expenseRepository,
                          ExpenseMapper expenseMapper) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper     = expenseMapper;
    }

    public Expense createExpense(
            BigDecimal amount,
            String description,
            LocalDate date,
            User user,
            Category category
    ) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description must not be empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category must not be null");
        }

        Expense expense = new Expense();
        expense.setAmount(amount);
        expense.setDescription(description);
        expense.setDate(date);
        expense.setUser(user);
        expense.setCategory(category);
        return expenseRepository.save(expense);
    }

    public Expense updateExpense(Long expenseId, ExpenseRequest request, User currentUser) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

        if (!expense.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not allowed to update this expense");
        }

        expense.setAmount(request.amount());
        expense.setDescription(request.description());
        expense.setDate(request.date());

        Category category = new Category();
        category.setId(request.categoryId());
        expense.setCategory(category);

        return expenseRepository.save(expense);
    }

    public void deleteExpense(Long expenseId, User currentUser) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

        if (!expense.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not allowed to delete this expense");
        }

        expenseRepository.delete(expense);
    }

    public record ExpensePage(
            List<Expense> content,
            long totalElements,
            int totalPages,
            BigDecimal totalSum
    ) {}

    public ExpensePage getFilteredExpenses(
            User user,
            LocalDate from,
            LocalDate to,
            Long categoryId,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            String sortBy,
            String order,
            int page,
            int size
    ) {
        Long userId = user.getId();

        Sort.Direction dir = order.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));

        List<Long> categoryIds = categoryId != null
                ? List.of(categoryId)
                : null;

        Specification<Expense> spec = ExpenseSpecification.withFilters(
                userId, from, to, categoryIds, minAmount, maxAmount
        );

        Page<Expense> pageEnt = expenseRepository.findAll(spec, pageable);

        BigDecimal sum = expenseRepository.sumByFilter(
                userId, from, to, categoryId, minAmount, maxAmount
        );
        if (sum == null) sum = BigDecimal.ZERO;

        return new ExpensePage(
                pageEnt.getContent(),
                pageEnt.getTotalElements(),
                pageEnt.getTotalPages(),
                sum
        );
    }

    public ExpensePageDto getFilteredExpensesDto(
            User user,
            LocalDate from,
            LocalDate to,
            Long categoryId,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            String sortBy,
            String order,
            int page,
            int size
    ) {
        ExpensePage ep = getFilteredExpenses(
                user, from, to,
                categoryId, minAmount, maxAmount,
                sortBy, order, page, size
        );

        List<ExpenseDto> dtos = ep.content().stream()
                .map(expenseMapper::toDto)
                .toList();

        return new ExpensePageDto(
                dtos,
                ep.totalPages(),
                ep.totalElements(),
                ep.totalSum()
        );
    }

    public List<ExpenseSummaryDto> getSummaryByCategory(User user) {
        return expenseRepository.sumByCategory(user);
    }
}
