package com.springliviu.expensetracker.repository;

import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.Expense;
import com.springliviu.expensetracker.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ExpenseRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Test
    @DisplayName("Находит расходы по пользователю")
    void findByUser_ReturnsExpenses() {
        User user = new User();
        user.setUsername("expenseUser");
        user.setPassword("pass");
        userRepository.save(user);

        Category category = new Category();
        category.setName("Food");
        category.setUser(user);
        categoryRepository.save(category);

        Expense expense = new Expense();
        expense.setAmount(new BigDecimal("50.00"));
        expense.setDescription("Lunch");
        expense.setDate(LocalDate.now());
        expense.setUser(user);
        expense.setCategory(category);
        expenseRepository.save(expense);

        List<Expense> result = expenseRepository.findByUser(user);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).isEqualTo("Lunch");
    }
}
