package com.springliviu.expensetracker.dto;

public class AuthRequest {
    private String username;
    private String password;

    /** Пустой конструктор для Jackson (обязателен) */
    public AuthRequest() {
    }

    /** Явный конструктор для тестов и ручного создания */
    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Геттеры и сеттеры

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
