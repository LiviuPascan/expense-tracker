package com.springliviu.expensetracker.controller;

import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.springliviu.expensetracker.dto.CategoryRequest;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Получить категории пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категории успешно получены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class)))
    })
    @GetMapping
    public ResponseEntity<List<Category>> getCategoriesByUser(
            @Parameter(description = "ID пользователя", example = "1")
            @RequestParam Long userId) {
        User user = new User();
        user.setId(userId);
        List<Category> categories = categoryService.getCategoriesByUser(user);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Создать новую категорию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория успешно создана",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "400", description = "Неверные данные", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Category> createCategory(
            @RequestBody CategoryRequest request) {
        User user = new User();
        user.setId(request.userId());
        Category created = categoryService.createCategory(request.name(), user);
        return ResponseEntity.ok(created);
    }
}
