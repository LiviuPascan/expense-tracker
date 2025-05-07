package com.springliviu.expensetracker.service;

import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateAndReturnCategory() {
        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setName("Food");
        category.setUser(user);

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.createCategory("Food", user);

        assertEquals("Food", result.getName());
        assertEquals(user, result.getUser());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        User user = new User();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                categoryService.createCategory("   ", user)
        );

        assertEquals("Category name must not be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                categoryService.createCategory("Transport", null)
        );

        assertEquals("User must not be null", exception.getMessage());
    }
}
