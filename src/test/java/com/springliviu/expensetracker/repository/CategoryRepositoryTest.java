package com.springliviu.expensetracker.repository;

import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.model.Role;           // ← вот так
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CategoryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Находит категории по пользователю")
    void findByUser_ReturnsCategories() {
        User user = new User();
        user.setUsername("categoryUser");
        user.setPassword("123");
        user.setRole(Role.USER);                        // ← обязательно
        user = userRepository.save(user);

        Category category = new Category();
        category.setName("Test Category");
        category.setUser(user);
        categoryRepository.save(category);

        List<Category> result = categoryRepository.findByUser(user);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Category");
    }
}
