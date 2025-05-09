package com.springliviu.expensetracker.controller;

import com.springliviu.expensetracker.security.UserDetailsImpl;
import com.springliviu.expensetracker.service.ExpenseService;
import com.springliviu.expensetracker.service.ExpenseService.ExpensePageDto;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ExpenseController.class)
@AutoConfigureMockMvc(addFilters = false)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseService expenseService;

    @Test
    @WithMockUser   // нужно, чтобы @AuthenticationPrincipal не был null, но фильтры отключены
    void getExpenses_withFilters_returnsExpensePageJson() throws Exception {
        // подготовка
        ExpensePageDto emptyDto = new ExpensePageDto(
                Collections.emptyList(), 0, 0, BigDecimal.ZERO
        );
        when(expenseService.getFilteredExpensesDto(
                any(), any(), any(), anyLong(), any(), any(),
                anyString(), anyString(), anyInt(), anyInt()
        )).thenReturn(emptyDto);

        // выполнение запроса
        mockMvc.perform(get("/api/expenses")
                        .param("from", "2025-01-01")
                        .param("to",   "2025-01-31")
                        .param("categoryId", "1")
                        .param("minAmount",  "10")
                        .param("maxAmount",  "100")
                        .param("sortBy",     "date")
                        .param("order",      "asc")
                        .param("page",       "0")
                        .param("size",       "5")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalSum").value(0));

        verify(expenseService).getFilteredExpensesDto(
                any(), any(), any(), anyLong(), any(), any(),
                anyString(), anyString(), anyInt(), anyInt()
        );
    }
}
