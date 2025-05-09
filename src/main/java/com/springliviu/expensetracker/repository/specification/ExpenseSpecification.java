package com.springliviu.expensetracker.repository.specification;

import com.springliviu.expensetracker.model.Expense;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ExpenseSpecification {

    /**
     * Собирает Specification по всем возможным фильтрам:
     *
     * @param userId       id пользователя
     * @param from         дата начала (включительно)
     * @param to           дата конца (включительно)
     * @param categoryIds  список id категорий (если пустой или null — не применяется)
     * @param minAmount    минимальная сумма (если null — не применяется)
     * @param maxAmount    максимальная сумма (если null — не применяется)
     */
    public static Specification<Expense> withFilters(
            Long userId,
            LocalDate from,
            LocalDate to,
            List<Long> categoryIds,
            BigDecimal minAmount,
            BigDecimal maxAmount
    ) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            // 1) только свои расходы
            predicates.getExpressions().add(
                    cb.equal(root.get("user").get("id"), userId)
            );

            // 2) по диапазону дат
            if (from != null) {
                predicates.getExpressions().add(
                        cb.greaterThanOrEqualTo(root.get("date"), from)
                );
            }
            if (to != null) {
                predicates.getExpressions().add(
                        cb.lessThanOrEqualTo(root.get("date"), to)
                );
            }

            // 3) по категориям
            if (categoryIds != null && !categoryIds.isEmpty()) {
                predicates.getExpressions().add(
                        root.get("category").get("id").in(categoryIds)
                );
            }

            // 4) по суммам
            if (minAmount != null) {
                predicates.getExpressions().add(
                        cb.greaterThanOrEqualTo(root.get("amount"), minAmount)
                );
            }
            if (maxAmount != null) {
                predicates.getExpressions().add(
                        cb.lessThanOrEqualTo(root.get("amount"), maxAmount)
                );
            }

            return predicates;
        };
    }
}
