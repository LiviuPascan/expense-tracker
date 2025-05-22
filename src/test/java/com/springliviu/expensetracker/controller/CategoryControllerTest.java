package com.springliviu.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springliviu.expensetracker.dto.CategoryRequest;
import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.security.Principal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    // Мок Principal
    private Principal mockPrincipal() {
        return () -> "testuser";
    }

    @Test
    void getCategoriesByUser_ShouldReturn200() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Food");

        when(categoryService.getCategoriesByUsername("testuser"))
                .thenReturn(List.of(category));

        mockMvc.perform(get("/api/categories")
                        .principal(mockPrincipal()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Food"));
    }

    @Test
    void createCategory_ShouldReturn200() throws Exception {
        CategoryRequest request = new CategoryRequest("Transport");

        Category category = new Category();
        category.setId(1L);
        category.setName("Transport");

        when(categoryService.createCategoryForUsername("Transport", "testuser"))
                .thenReturn(category);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .principal(mockPrincipal()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Transport"));
    }

    @Test
    void deleteCategory_ShouldReturn204() throws Exception {
        // мок роли ADMIN
        var authentication = org.springframework.security.authentication.
                UsernamePasswordAuthenticationToken.authenticated(
                        "testuser", null, List.of(() -> "ROLE_ADMIN"));

        org.springframework.security.core.context.SecurityContextHolder
                .getContext().setAuthentication(authentication);

        mockMvc.perform(delete("/api/categories/1")
                        .principal(mockPrincipal()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCategory_ShouldReturn403_WhenNotAdmin() throws Exception {
        var authentication = org.springframework.security.authentication.
                UsernamePasswordAuthenticationToken.authenticated(
                        "testuser", null, List.of(() -> "ROLE_USER"));

        org.springframework.security.core.context.SecurityContextHolder
                .getContext().setAuthentication(authentication);

        mockMvc.perform(delete("/api/categories/1")
                        .principal(mockPrincipal()))
                .andExpect(status().isForbidden());
    }
}
