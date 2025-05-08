package com.springliviu.expensetracker.repository.specification;

import com.springliviu.expensetracker.model.Expense;
import com.springliviu.expensetracker.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseSpecification {
    public static Specification<Expense> withFilters(
            User user,
            LocalDate from,
            LocalDate to,
            Long categoryId,
            BigDecimal minAmount,
            BigDecimal maxAmount
    ) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            predicates.getExpressions().add(cb.equal(root.get("user"), user));

            if (from != null) {
                predicates.getExpressions().add(cb.greaterThanOrEqualTo(root.get("date"), from));
            }
            if (to != null) {
                predicates.getExpressions().add(cb.lessThanOrEqualTo(root.get("date"), to));
            }
            if (categoryId != null) {
                predicates.getExpressions().add(cb.equal(root.get("category").get("id"), categoryId));
            }
            if (minAmount != null) {
                predicates.getExpressions().add(cb.greaterThanOrEqualTo(root.get("amount"), minAmount));
            }
            if (maxAmount != null) {
                predicates.getExpressions().add(cb.lessThanOrEqualTo(root.get("amount"), maxAmount));
            }

            return predicates;
        };
    }
}
