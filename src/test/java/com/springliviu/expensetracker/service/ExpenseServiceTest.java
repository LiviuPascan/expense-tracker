package com.springliviu.expensetracker.service;

import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.Expense;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldThrowExceptionWhenAmountIsInvalid() {
        User user = new User();
        Category category = new Category();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            expenseService.createExpense(BigDecimal.ZERO, "Lunch", LocalDate.now(), user, category);
        });

        assertEquals("Amount must be greater than zero", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsEmpty() {
        User user = new User();
        Category category = new Category();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            expenseService.createExpense(BigDecimal.valueOf(50), "   ", LocalDate.now(), user, category);
        });

        assertEquals("Description must not be empty", exception.getMessage());
    }


    @Test
    void shouldCreateAndReturnExpense() {
        User user = new User();
        user.setId(1L);
        Category category = new Category();
        category.setId(1L);
        Expense expense = new Expense();
        expense.setAmount(BigDecimal.valueOf(100));
        expense.setDescription("Lunch");
        expense.setDate(LocalDate.now());
        expense.setUser(user);
        expense.setCategory(category);

        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        Expense result = expenseService.createExpense(
                BigDecimal.valueOf(100),
                "Lunch",
                LocalDate.now(),
                user,
                category
        );

        assertEquals(BigDecimal.valueOf(100), result.getAmount());
        assertEquals("Lunch", result.getDescription());
        assertEquals(user, result.getUser());
        assertEquals(category, result.getCategory());
        verify(expenseRepository).save(any(Expense.class));
    }
}
