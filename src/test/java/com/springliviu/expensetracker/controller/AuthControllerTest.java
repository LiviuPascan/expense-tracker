package com.springliviu.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springliviu.expensetracker.dto.AuthRequest;
import com.springliviu.expensetracker.dto.AuthResponse;
import com.springliviu.expensetracker.exception.UsernameAlreadyExistsException;
import com.springliviu.expensetracker.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        com.springliviu.expensetracker.security.JwtAuthenticationFilter.class,
                        com.springliviu.expensetracker.security.SecurityConfig.class
                }
        )
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void register_Success_Returns200() throws Exception {
        AuthRequest req = new AuthRequest("alice", "pwd");

        // ничего не кидает — будет OK
        Mockito.doNothing().when(authService).register("alice", "pwd");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void register_UsernameExists_Returns400() throws Exception {
        AuthRequest req = new AuthRequest("bob", "pwd");
        Mockito.doThrow(new UsernameAlreadyExistsException())
                .when(authService).register("bob", "pwd");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));
    }

    @Test
    void register_InvalidInput_Returns400() throws Exception {
        AuthRequest req = new AuthRequest("  ", "   ");
        // сервис выбросит IllegalArgumentException
        Mockito.doThrow(new IllegalArgumentException("Username must not be empty"))
                .when(authService).register(anyString(), anyString());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username must not be empty"));
    }

    @Test
    void login_Success_ReturnsJwt() throws Exception {
        AuthRequest req = new AuthRequest("alice", "pwd");
        String fakeToken = "jwt.token.here";
        Mockito.when(authService.login("alice", "pwd"))
                .thenReturn(fakeToken);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(fakeToken));
    }

    @Test
    void login_BadCredentials_Returns401() throws Exception {
        AuthRequest req = new AuthRequest("alice", "wrong");
        // симулируем падение аутентификации
        Mockito.when(authService.login("alice", "wrong"))
                .thenThrow(new org.springframework.security.core.AuthenticationException("Bad") {});

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_InvalidInput_Returns400() throws Exception {
        AuthRequest req = new AuthRequest("", "");
        Mockito.when(authService.login(anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Username must not be empty"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}
