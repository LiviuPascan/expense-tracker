package com.springliviu.expensetracker.service;

import com.springliviu.expensetracker.exception.NotFoundException;
import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.repository.CategoryRepository;
import com.springliviu.expensetracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Категория с ID " + id + " не найдена"));
    }

    public Category createCategoryForUsername(String name, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + username));

        Category category = new Category();
        category.setName(name);
        category.setUser(user);

        return categoryRepository.save(category);
    }

    public List<Category> getCategoriesByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + username));

        return categoryRepository.findByUser(user);
    }

    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Категория с ID " + id + " не найдена");
        }
        categoryRepository.deleteById(id);
    }
}
