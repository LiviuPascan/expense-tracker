package com.springliviu.expensetracker.controller;

import com.springliviu.expensetracker.dto.CategoryRequest;
import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.security.UserDetailsImpl;
import com.springliviu.expensetracker.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Получить категории текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категории успешно получены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class)))
    })
    @GetMapping
    public ResponseEntity<List<Category>> getCategoriesByUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
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
            @RequestBody CategoryRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        Category created = categoryService.createCategory(request.name(), user);
        return ResponseEntity.ok(created);
    }
}
