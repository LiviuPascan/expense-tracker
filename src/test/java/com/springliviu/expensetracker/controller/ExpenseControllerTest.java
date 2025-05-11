package com.springliviu.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springliviu.expensetracker.dto.ExpenseRequest;
import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.Expense;
import com.springliviu.expensetracker.model.Role;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.security.UserDetailsImpl;
import com.springliviu.expensetracker.service.ExpenseService;
import com.springliviu.expensetracker.mapper.ExpenseMapper;
import com.springliviu.expensetracker.dto.ExpenseDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ExpenseControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private ExpenseService expenseService;
    @MockBean private ExpenseMapper expenseMapper;

    private UserDetailsImpl getMockUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("karina");
        user.setRole(Role.USER);
        return new UserDetailsImpl(user);
    }

    @Test
    void shouldCreateExpense() throws Exception {
        ExpenseRequest request = new ExpenseRequest(
                new BigDecimal("123.45"),
                "Lunch",
                LocalDate.now(),
                1L,
                2L
        );

        Category category = new Category();
        category.setId(2L);
        category.setName("Food");

        User user = getMockUser().getUser();

        Expense expense = new Expense();
        expense.setId(100L);
        expense.setAmount(request.amount());
        expense.setDescription(request.description());
        expense.setDate(request.date());
        expense.setUser(user);
        expense.setCategory(category);

        ExpenseDto dto = new ExpenseDto(
                100L,
                request.amount(),
                request.description(),
                request.date(),
                2L,
                "Food"
        );

        Mockito.when(expenseService.createExpense(
                any(), any(), any(), any(), any()
        )).thenReturn(expense);

        Mockito.when(expenseMapper.toDto(expense)).thenReturn(dto);

        mockMvc.perform(post("/api/expenses")
                        .with(user(getMockUser()))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Lunch"))
                .andExpect(jsonPath("$.amount").value(123.45))
                .andExpect(jsonPath("$.categoryName").value("Food"));
    }
}
