package com.springliviu.expensetracker.repository;

import com.springliviu.expensetracker.dto.ExpenseSummaryDto;
import com.springliviu.expensetracker.model.Expense;
import com.springliviu.expensetracker.model.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository
        extends JpaRepository<Expense, Long>,
        JpaSpecificationExecutor<Expense> {

    /**
     * Получить все расходы заданного пользователя.
     */
    List<Expense> findByUser(User user);

    /**
     * Суммирует amount у всех расходов пользователя с учётом опциональных фильтров.
     * Если хочешь учитывать список категорий, можно добавить перегрузку или расширить этот запрос.
     */
    @Query("""
        SELECT SUM(e.amount)
        FROM Expense e
        WHERE e.user.id = :userId
          AND (:from       IS NULL OR e.date       >= :from)
          AND (:to         IS NULL OR e.date       <= :to)
          AND (:categoryId IS NULL OR e.category.id = :categoryId)
          AND (:minAmount  IS NULL OR e.amount     >= :minAmount)
          AND (:maxAmount  IS NULL OR e.amount     <= :maxAmount)
        """)
    BigDecimal sumByFilter(
            @Param("userId")     Long userId,
            @Param("from")       LocalDate from,
            @Param("to")         LocalDate to,
            @Param("categoryId") Long categoryId,
            @Param("minAmount")  BigDecimal minAmount,
            @Param("maxAmount")  BigDecimal maxAmount
    );

    @Query("""
    SELECT new com.springliviu.expensetracker.dto.ExpenseSummaryDto(
        e.category.name,
        SUM(e.amount)
    )
    FROM Expense e
    WHERE e.user = :user
    GROUP BY e.category.name
""")
    List<ExpenseSummaryDto> sumByCategory(@Param("user") User user);

}
