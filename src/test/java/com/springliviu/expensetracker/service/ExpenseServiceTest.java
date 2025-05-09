package com.springliviu.expensetracker.service;

import com.springliviu.expensetracker.model.Expense;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.repository.ExpenseRepository;
import com.springliviu.expensetracker.mapper.ExpenseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ExpenseMapper expenseMapper;

    private ExpenseService expenseService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        expenseService = new ExpenseService(expenseRepository, expenseMapper);
    }

    @Test
    void getFilteredExpenses_whenNoData_thenEmptyPageWithZeroSum() {
        // given
        User user = new User();
        user.setId(1L);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Expense> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        // мокируем любой Specification и любой Pageable
        when(expenseRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(emptyPage);
        when(expenseRepository.sumByFilter(1L, null, null, null, null, null))
                .thenReturn(null);

        // when
        ExpenseService.ExpensePage result = expenseService.getFilteredExpenses(
                user, null, null, null, null, null,
                "date", "asc", 0, 10
        );

        // then
        assertTrue(result.content().isEmpty());
        assertEquals(0, result.totalElements());
        assertEquals(0, result.totalPages());
        assertEquals(BigDecimal.ZERO, result.totalSum());

        verify(expenseRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(expenseRepository).sumByFilter(1L, null, null, null, null, null);
    }

    @Test
    void getFilteredExpenses_withData_thenCorrectPageAndSum() {
        // given
        User user = new User();
        user.setId(2L);

        LocalDate from       = LocalDate.of(2025, 1, 1);
        LocalDate to         = LocalDate.of(2025, 1, 31);
        Long categoryId      = 5L;
        BigDecimal minAmount = BigDecimal.valueOf(10);
        BigDecimal maxAmount = BigDecimal.valueOf(100);

        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "amount"));
        Expense e1 = new Expense(); e1.setId(1L); e1.setAmount(BigDecimal.valueOf(50));
        Expense e2 = new Expense(); e2.setId(2L); e2.setAmount(BigDecimal.valueOf(75));
        Page<Expense> page = new PageImpl<>(List.of(e1, e2), pageable, 2);

        when(expenseRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);
        when(expenseRepository.sumByFilter(2L, from, to, categoryId, minAmount, maxAmount))
                .thenReturn(BigDecimal.valueOf(125));

        // when
        ExpenseService.ExpensePage result = expenseService.getFilteredExpenses(
                user, from, to, categoryId, minAmount, maxAmount,
                "amount", "desc", 0, 2
        );

        // then
        assertEquals(2, result.content().size());
        assertEquals(2, result.totalElements());
        assertEquals(1, result.totalPages());
        assertEquals(BigDecimal.valueOf(125), result.totalSum());

        verify(expenseRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(expenseRepository).sumByFilter(2L, from, to, categoryId, minAmount, maxAmount);
    }
}
