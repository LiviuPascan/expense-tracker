package com.springliviu.expensetracker.dto;

import java.math.BigDecimal;
import java.util.List;

public class ExpensePageDto {
    private List<ExpenseDto> content;
    private int totalPages;
    private long totalElements;
    private BigDecimal totalSum;

    public ExpensePageDto(List<ExpenseDto> content, int totalPages, long totalElements, BigDecimal totalSum) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.totalSum = totalSum;
    }

    public List<ExpenseDto> getContent() {
        return content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public BigDecimal getTotalSum() {
        return totalSum;
    }
}
