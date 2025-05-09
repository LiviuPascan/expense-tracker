package com.springliviu.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springliviu.expensetracker.dto.CategoryRequest;
import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnCategoriesByUser() throws Exception {
        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(1L);
        category.setName("Food");
        category.setUser(user);

        when(categoryService.getCategoriesByUser(any(User.class)))
                .thenReturn(List.of(category));

        mockMvc.perform(get("/api/categories")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Food"));
    }

    @Test
    void shouldCreateCategory() throws Exception {
        CategoryRequest request = new CategoryRequest("Transport");

        Category category = new Category();
        category.setId(2L);
        category.setName("Transport");
        category.setUser(new User());

        when(categoryService.createCategory(any(String.class), any(User.class)))
                .thenReturn(category);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Transport"));
    }
}
