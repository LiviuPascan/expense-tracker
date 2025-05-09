package com.springliviu.expensetracker.service;

import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createOrFetchUser(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username must not be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password must not be empty");
        }

        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            System.out.println("ℹ️ Пользователь " + username + " уже существует.");
            return existing.get();
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // 🔐 лучше использовать passwordEncoder, если это прод
        userRepository.save(user);
        System.out.println("✅ Пользователь создан и сохранён.");
        return user;
    }

    // 🔐 Метод только для ADMIN — получить всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
