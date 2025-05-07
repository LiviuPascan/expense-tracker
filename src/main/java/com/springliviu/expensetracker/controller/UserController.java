package com.springliviu.expensetracker.controller;

import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createOrFetchUser(@RequestBody UserRequest request) {
        User user = userService.createOrFetchUser(request.username(), request.password());
        return ResponseEntity.ok(user);
    }

    public record UserRequest(String username, String password) {}
}
