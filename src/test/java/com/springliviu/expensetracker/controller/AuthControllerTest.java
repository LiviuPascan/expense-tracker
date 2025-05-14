package com.springliviu.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springliviu.expensetracker.dto.AuthRequest;
import com.springliviu.expensetracker.model.Role;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test // Успешная регистрация нового пользователя
    void register_ShouldReturn200() throws Exception {
        AuthRequest request = new AuthRequest("alice", "password");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test // Регистрация с существующим username должна вернуть 400
    void register_ShouldReturn400_WhenUsernameExists() throws Exception {
        User user = new User();
        user.setUsername("bob");
        user.setPassword(passwordEncoder.encode("pass"));
        user.setRole(Role.USER);
        userRepository.save(user);

        AuthRequest request = new AuthRequest("bob", "password");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test // Регистрация с пустым username должна вернуть 400
    void register_ShouldReturn400_WhenUsernameEmpty() throws Exception {
        AuthRequest request = new AuthRequest("", "password");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test // Регистрация с пустым паролем должна вернуть 400
    void register_ShouldReturn400_WhenPasswordEmpty() throws Exception {
        AuthRequest request = new AuthRequest("testuser", "");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test // Успешный вход с корректными данными
    void login_ShouldReturn200_WhenCredentialsAreValid() throws Exception {
        User user = new User();
        user.setUsername("carol");
        user.setPassword(passwordEncoder.encode("pass123"));
        user.setRole(Role.USER);
        userRepository.save(user);

        AuthRequest request = new AuthRequest("carol", "pass123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test // Неверный пароль при логине должен вернуть 401
    void login_ShouldReturn401_WhenPasswordIncorrect() throws Exception {
        User user = new User();
        user.setUsername("dan");
        user.setPassword(passwordEncoder.encode("correct"));
        user.setRole(Role.USER);
        userRepository.save(user);

        AuthRequest request = new AuthRequest("dan", "wrong");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test // Попытка входа с несуществующим пользователем должна вернуть 401
    void login_ShouldReturn401_WhenUsernameNotFound() throws Exception {
        AuthRequest request = new AuthRequest("unknown", "whatever");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test // Вход с пустым username должен вернуть 400
    void login_ShouldReturn400_WhenUsernameEmpty() throws Exception {
        AuthRequest request = new AuthRequest("", "password");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test // Вход с пустым паролем должен вернуть 400
    void login_ShouldReturn400_WhenPasswordEmpty() throws Exception {
        AuthRequest request = new AuthRequest("user", "");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
