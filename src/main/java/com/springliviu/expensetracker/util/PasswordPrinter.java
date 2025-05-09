package com.springliviu.expensetracker.util;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordPrinter {

    @PostConstruct
    public void printPasswordHash() {
        String rawPassword = "admin123";
        String encoded = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println("🔐 Хеш пароля admin123:");
        System.out.println(encoded);
    }
}
