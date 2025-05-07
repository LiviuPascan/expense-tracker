package com.springliviu.expensetracker.service;

import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.Expense;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.repository.ExpenseRepository;
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
}
