package com.springliviu.expensetracker.repository;

import com.springliviu.expensetracker.model.Expense;
import com.springliviu.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);
    List<Expense> findByUserAndDateBetween(User user, LocalDate from, LocalDate to);
}