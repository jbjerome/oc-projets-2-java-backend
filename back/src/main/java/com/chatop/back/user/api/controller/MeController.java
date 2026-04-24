package com.chatop.back.user.api.controller;

import com.chatop.back.user.api.response.UserResponse;
import com.chatop.back.user.application.usecase.GetCurrentUserUseCase;
import com.chatop.back.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller returning information about the authenticated user.
 *
 * <p>Identity is read from the JWT {@code sub} claim injected by Spring
 * Security via {@code @AuthenticationPrincipal}. No ID lookup or parameter
 * is exposed — you can only see your own profile.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication and current user endpoints")
public class MeController {

    private final GetCurrentUserUseCase getCurrentUserUseCase;

    public MeController(GetCurrentUserUseCase getCurrentUserUseCase) {
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }

    /**
     * Returns the current user's profile.
     *
     * @param jwt validated JWT injected by Spring Security
     * @return a {@link UserResponse} without the password hash
     * @throws com.chatop.back.user.domain.exception.UserNotFoundException if the account
     *         associated with the JWT no longer exists in the database (HTTP 404)
     */
    @GetMapping("/me")
    @Operation(summary = "Retrieve the authenticated user's profile")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User profile",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Success",
                                    value = "{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john@test.com\", \"created_at\": \"2024-01-15T10:00:00Z\", \"updated_at\": \"2024-01-15T10:00:00Z\"}"
                            )
                    )
            )
    })
    public UserResponse me(@AuthenticationPrincipal Jwt jwt) {
        User user = getCurrentUserUseCase.handle(jwt.getSubject());
        return UserResponse.builder()
                .id(user.id())
                .name(user.name())
                .email(user.email())
                .createdAt(user.createdAt())
                .updatedAt(user.updatedAt())
                .build();
    }
}
