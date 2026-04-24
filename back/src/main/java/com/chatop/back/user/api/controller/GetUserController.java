package com.chatop.back.user.api.controller;

import com.chatop.back.user.api.response.UserResponse;
import com.chatop.back.user.application.usecase.GetUserByIdUseCase;
import com.chatop.back.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "User lookup endpoints")
public class GetUserController {

    private final GetUserByIdUseCase getUserByIdUseCase;

    public GetUserController(GetUserByIdUseCase getUserByIdUseCase) {
        this.getUserByIdUseCase = getUserByIdUseCase;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch a user by id")
    public UserResponse getById(@PathVariable Long id) {
        User user = getUserByIdUseCase.handle(id);
        return UserResponse.builder()
                .id(user.id())
                .name(user.name())
                .email(user.email())
                .createdAt(user.createdAt())
                .updatedAt(user.updatedAt())
                .build();
    }
}
