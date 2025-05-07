package com.springliviu.expensetracker.controller;

import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.service.UserService;
import com.springliviu.expensetracker.dto.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserControllerTest{

    private final UserService userService;

    public UserControllerTest(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Создать нового пользователя или вернуть существующего")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь создан или найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Неверные данные", content = @Content)
    })
    @PostMapping
    public ResponseEntity<User> createOrFetchUser(
            @RequestBody UserRequest request) {
        User user = userService.createOrFetchUser(request.username(), request.password());
        return ResponseEntity.ok(user);
    }
}

