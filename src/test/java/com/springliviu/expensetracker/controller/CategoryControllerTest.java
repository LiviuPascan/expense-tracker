package com.springliviu.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springliviu.expensetracker.dto.CategoryRequest;
import com.springliviu.expensetracker.model.Category;
import com.springliviu.expensetracker.model.Role;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.security.UserDetailsImpl;
import com.springliviu.expensetracker.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    private UserDetailsImpl getMockUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("karina");
        user.setRole(Role.USER);
        return new UserDetailsImpl(user);
    }

    @Test
    void shouldReturnCategoriesByUser() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Food");
        category.setUser(getMockUser().getUser());

        Mockito.when(categoryService.getCategoriesByUser(any(User.class)))
                .thenReturn(List.of(category));

        mockMvc.perform(get("/api/categories")
                        .with(user(getMockUser())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Food"));
    }

    @Test
    void shouldCreateCategory() throws Exception {
        CategoryRequest request = new CategoryRequest("Transport");

        Category category = new Category();
        category.setId(2L);
        category.setName("Transport");
        category.setUser(getMockUser().getUser());

        Mockito.when(categoryService.createCategory(any(String.class), any(User.class)))
                .thenReturn(category);

        mockMvc.perform(post("/api/categories")
                        .with(user(getMockUser()))
                        .with(csrf()) // ← CSRF добавлен
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Transport"));
    }
}
