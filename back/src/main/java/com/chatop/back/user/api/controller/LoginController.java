package com.chatop.back.user.api.controller;

import com.chatop.back.user.api.request.LoginRequest;
import com.chatop.back.user.api.response.LoginResponse;
import com.chatop.back.user.application.command.LoginUserCommand;
import com.chatop.back.user.application.usecase.LoginUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller dedicated to authenticating an existing user.
 *
 * Single endpoint: {@code POST /api/auth/login}. For security reasons
 * (to prevent email enumeration), "unknown email" is not distinguished
 * from "wrong password": both return a 401.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication and current user endpoints")
public class LoginController {

    private final LoginUserUseCase loginUserUseCase;

    public LoginController(LoginUserUseCase loginUserUseCase) {
        this.loginUserUseCase = loginUserUseCase;
    }

    /**
     * Verifies credentials and returns a signed JWT.
     *
     * @param request validated payload containing {@code email} and {@code password}
     * @return a {@link LoginResponse} containing the JWT
     * @throws com.chatop.back.user.domain.exception.InvalidCredentialsException if the
     *         email or password does not match (HTTP 401)
     */
    @PostMapping("/login")
    @SecurityRequirements
    @Operation(summary = "Authenticate a user and return a JWT")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Valid credentials, JWT issued",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Success",
                                    value = "{\"token\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQHRlc3QuY29tIiwiaWF0IjoxNzEyMDAwMDAwLCJleHAiOjE3MTIwMDM2MDB9.signature\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid payload (validation)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Malformed email",
                                    value = "{\"email\": \"must be a well-formed email address\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Wrong email or password",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Invalid credentials",
                                    value = "{\"message\": \"error\"}"
                            )
                    )
            )
    })
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        String jwt = loginUserUseCase.handle(
                new LoginUserCommand(request.getEmail(), request.getPassword())
        );
        return LoginResponse.builder().token(jwt).build();
    }
}
