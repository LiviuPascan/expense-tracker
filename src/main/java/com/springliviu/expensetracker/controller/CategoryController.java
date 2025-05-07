package com.springliviu.expensetracker.controller;

import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategoriesByUser(@RequestParam Long userId) {
        User user = new User();
        user.setId(userId);
        List<Category> categories = categoryService.getCategoriesByUser(user);
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest request) {
        User user = new User();
        user.setId(request.userId());
        Category created = categoryService.createCategory(request.name(), user);
        return ResponseEntity.ok(created);
    }

    public record CategoryRequest(String name, Long userId) {
    }
}
