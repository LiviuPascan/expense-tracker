package com.springliviu.expensetracker.service;

import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnExistingUserIfExists() {
        User existingUser = new User();
        existingUser.setUsername("john");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(existingUser));

        User result = userService.createOrFetchUser("john", "any");

        assertEquals("john", result.getUsername());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldCreateNewUserIfNotExists() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());

        User savedUser = new User();
        savedUser.setUsername("alice");
        savedUser.setPassword("pass123");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createOrFetchUser("alice", "pass123");

        assertEquals("alice", result.getUsername());
        assertEquals("pass123", result.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsEmpty() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                userService.createOrFetchUser("   ", "pass")
        );
        assertEquals("Username must not be empty", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsEmpty() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                userService.createOrFetchUser("user", "   ")
        );
        assertEquals("Password must not be empty", ex.getMessage());
    }
}
