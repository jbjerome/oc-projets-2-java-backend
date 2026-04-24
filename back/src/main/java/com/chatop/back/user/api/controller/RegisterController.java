package com.chatop.back.user.api.controller;

import com.chatop.back.user.api.request.RegisterRequest;
import com.chatop.back.user.api.response.RegisterResponse;
import com.chatop.back.user.application.command.RegisterUserCommand;
import com.chatop.back.user.application.usecase.RegisterUserUseCase;
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
 * REST controller dedicated to registering a new user.
 *
 * <p>Single endpoint: {@code POST /api/auth/register}. Login and the
 * current-user profile are handled by separate controllers.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication and current user endpoints")
public class RegisterController {

    private final RegisterUserUseCase registerUserUseCase;

    public RegisterController(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    /**
     * Creates a new user account and returns a signed JWT.
     *
     * @param request validated payload containing {@code name}, {@code email} and {@code password}
     * @return a {@link RegisterResponse} containing the JWT to use in the {@code Authorization} header of requests
     * @throws com.chatop.back.user.domain.exception.EmailAlreadyUsedException if email already used (HTTP 400)
     */
    @PostMapping("/register")
    @SecurityRequirements
    @Operation(summary = "Create a user account and return a JWT")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Account created, JWT issued",
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
                    description = "Invalid payload (validation) or email already used",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Malformed email",
                                            value = "{\"email\": \"must be a well-formed email address\"}"
                                    )
                            }
                    )
            )
    })
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        String jwt = registerUserUseCase.handle(
                new RegisterUserCommand(request.getName(), request.getEmail(), request.getPassword())
        );
        return RegisterResponse.builder().token(jwt).build();
    }
}
