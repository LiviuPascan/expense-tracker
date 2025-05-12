package com.springliviu.expensetracker.dto;

import java.math.BigDecimal;

public class ExpenseSummaryDto {
    private String category;
    private BigDecimal total;

    public ExpenseSummaryDto(String category, BigDecimal total) {
        this.category = category;
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
