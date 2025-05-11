package com.springliviu.expensetracker.service;

import com.springliviu.expensetracker.exception.UsernameAlreadyExistsException;
import com.springliviu.expensetracker.model.Role;
import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.repository.UserRepository;
import com.springliviu.expensetracker.security.JwtTokenProvider;
import com.springliviu.expensetracker.security.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    // Явный конструктор инициализирует все final-поля
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Регистрирует нового пользователя.
     * @throws IllegalArgumentException, если username или password пустые.
     * @throws UsernameAlreadyExistsException, если пользователь с таким именем уже есть.
     */
    public void register(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username must not be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password must not be empty");
        }

        userRepository.findByUsername(username)
                .ifPresent(u -> { throw new UsernameAlreadyExistsException(); });

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    /**
     * Аутентифицирует пользователя и возвращает JWT.
     * @throws IllegalArgumentException, если username или password пустые.
     * @throws org.springframework.security.core.AuthenticationException, если аутентификация неуспешна.
     */
    public String login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username must not be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password must not be empty");
        }

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return jwtTokenProvider.generateToken(userDetails);
    }
}
