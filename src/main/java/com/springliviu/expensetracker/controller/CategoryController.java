package com.springliviu.expensetracker.controller;

import com.springliviu.expensetracker.dto.CategoryRequest;
import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    public ResponseEntity<List<Category>> getCategoriesByUser(Principal principal) {
        List<Category> categories = categoryService.getCategoriesByUsername(principal.getName());
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
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest request,
                                                   Principal principal) {
        Category created = categoryService.createCategoryForUsername(request.name(), principal.getName());
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Удалить категорию по ID (только для ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Категория удалена"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id,
                                               Principal principal) {
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
