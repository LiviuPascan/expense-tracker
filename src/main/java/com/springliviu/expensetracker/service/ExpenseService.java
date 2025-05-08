package com.springliviu.expensetracker.service;

import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.Expense;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.repository.ExpenseRepository;
import com.springliviu.expensetracker.repository.specification.ExpenseSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense createExpense(BigDecimal amount, String description, LocalDate date, User user, Category category) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (description == null || description.trim().isEmpty()) {
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

    public List<Expense> getExpensesByUser(User user) {
        return expenseRepository.findByUser(user);
    }

    public Page<Expense> getFilteredExpenses(
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
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<Expense> spec = ExpenseSpecification.withFilters(user, from, to, categoryId, minAmount, maxAmount);
        return expenseRepository.findAll(spec, pageable);
    }
}
