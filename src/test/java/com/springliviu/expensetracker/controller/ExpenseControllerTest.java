package com.springliviu.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springliviu.expensetracker.dto.ExpenseRequest;
import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.Expense;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.springliviu.expensetracker.dto.ExpenseRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @MockBean
    private ExpenseService expenseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnExpensesForUser() throws Exception {
        User user = new User();
        user.setId(1L);

        Expense expense = new Expense();
        expense.setId(1L);
        expense.setDescription("Lunch");
        expense.setAmount(BigDecimal.valueOf(50));
        expense.setDate(LocalDate.of(2025, 5, 7));
        expense.setUser(user);

        Mockito.when(expenseService.getExpensesByUser(any(User.class)))
                .thenReturn(List.of(expense));

        mockMvc.perform(get("/api/expenses")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Lunch"));
    }

    @Test
    void shouldCreateExpense() throws Exception {
        ExpenseRequest request = new ExpenseRequest(
                BigDecimal.valueOf(120.5),
                "Taxi",
                LocalDate.of(2025, 5, 7),
                1L,
                2L
        );

        User user = new User();
        user.setId(1L);
        Category category = new Category();
        category.setId(2L);

        Expense expense = new Expense();
        expense.setId(1L);
        expense.setAmount(request.amount());
        expense.setDescription(request.description());
        expense.setDate(request.date());
        expense.setUser(user);
        expense.setCategory(category);

        Mockito.when(expenseService.createExpense(
                any(BigDecimal.class),
                any(String.class),
                any(LocalDate.class),
                any(User.class),
                any(Category.class)
        )).thenReturn(expense);

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Taxi"))
                .andExpect(jsonPath("$.amount").value(120.5));
    }
}
