package com.chatop.back.auth.controller;

import com.chatop.back.auth.service.AuthService;
import com.chatop.back.user.dto.LoginRequest;
import com.chatop.back.user.dto.RegisterRequest;
import com.chatop.back.user.dto.TokenResponse;
import com.chatop.back.user.dto.UserResponse;
import com.chatop.back.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication and current user endpoints")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    @SecurityRequirements
    @Operation(summary = "Create a user account and return a JWT")
    public TokenResponse register(@Valid @RequestBody RegisterRequest request) {
        return TokenResponse.builder().token(authService.register(request)).build();
    }

    @PostMapping("/login")
    @SecurityRequirements
    @Operation(summary = "Authenticate a user and return a JWT")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return TokenResponse.builder().token(authService.login(request)).build();
    }

    @GetMapping("/me")
    @Operation(summary = "Retrieve the authenticated user's profile")
    public UserResponse me(@AuthenticationPrincipal Jwt jwt) {
        return UserResponse.from(userService.getByEmail(jwt.getSubject()));
    }
}
