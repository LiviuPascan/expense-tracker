package com.springliviu.expensetracker.controller;

import com.springliviu.expensetracker.dto.ExpensePageDto;
import com.springliviu.expensetracker.model.Role;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.security.UserDetailsImpl;
import com.springliviu.expensetracker.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseService expenseService;

    @Test
    void getExpenses_withFilters_returnsExpensePageJson() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("karina");
        user.setRole(Role.USER);

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        ExpensePageDto dto = new ExpensePageDto(
                Collections.emptyList(), 0, 0, BigDecimal.ZERO
        );

        Mockito.when(expenseService.getFilteredExpensesDto(
                any(User.class),
                any(), any(), any(), any(), any(),
                any(), any(), anyInt(), anyInt()
        )).thenReturn(dto);

        mockMvc.perform(get("/api/expenses")
                        .with(user(userDetails))
                        .param("from", "2025-01-01")
                        .param("to", "2025-01-31")
                        .param("categoryId", "1")
                        .param("minAmount", "10")
                        .param("maxAmount", "100")
                        .param("sortBy", "date")
                        .param("order", "asc")
                        .param("page", "0")
                        .param("size", "5"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalSum").value(0));
    }
}
