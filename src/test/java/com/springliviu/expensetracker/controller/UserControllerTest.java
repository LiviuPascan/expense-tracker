package com.springliviu.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateOrFetchUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("karina");
        user.setPassword("secret");

        UserController.UserRequest request = new UserController.UserRequest("karina", "secret");

        Mockito.when(userService.createOrFetchUser("karina", "secret")).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("karina"));
    }
}
