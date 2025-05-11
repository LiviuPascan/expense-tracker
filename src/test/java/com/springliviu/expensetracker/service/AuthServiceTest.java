package com.springliviu.expensetracker.service;

import com.springliviu.expensetracker.exception.UsernameAlreadyExistsException;
import com.springliviu.expensetracker.model.Role;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.repository.UserRepository;
import com.springliviu.expensetracker.security.JwtTokenProvider;
import com.springliviu.expensetracker.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- register tests ---

    @Test
    void register_ThrowsWhenUsernameEmpty() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register("   ", "pass")
        );
        assertEquals("Username must not be empty", ex.getMessage());
    }

    @Test
    void register_ThrowsWhenPasswordEmpty() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register("user", "   ")
        );
        assertEquals("Password must not be empty", ex.getMessage());
    }

    @Test
    void register_ThrowsWhenUsernameExists() {
        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(new User()));
        assertThrows(
                UsernameAlreadyExistsException.class,
                () -> authService.register("john", "pwd")
        );
        verify(userRepository).findByUsername("john");
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_SuccessSavesUserWithEncodedPasswordAndRole() {
        when(userRepository.findByUsername("alice"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret")).thenReturn("encodedSecret");

        authService.register("alice", "secret");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertEquals("alice", saved.getUsername());
        assertEquals("encodedSecret", saved.getPassword());
        assertEquals(Role.USER, saved.getRole());
    }

    // --- login tests ---

    @Test
    void login_ThrowsWhenUsernameEmpty() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.login("  ", "pass")
        );
        assertEquals("Username must not be empty", ex.getMessage());
    }

    @Test
    void login_ThrowsWhenPasswordEmpty() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.login("user", "")
        );
        assertEquals("Password must not be empty", ex.getMessage());
    }

    @Test
    void login_ThrowsWhenAuthenticationFails() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(mock(AuthenticationException.class));

        assertThrows(
                AuthenticationException.class,
                () -> authService.login("bob", "wrong")
        );
    }

    @Test
    void login_SuccessReturnsJwt() {
        User user = new User();
        user.setUsername("carol");
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(jwtTokenProvider.generateToken(userDetails))
                .thenReturn("jwt-token-123");

        String token = authService.login("carol", "pwd");
        assertEquals("jwt-token-123", token);
    }
}
