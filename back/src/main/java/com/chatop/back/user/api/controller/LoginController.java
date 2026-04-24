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
 * Contrôleur REST dédié à l'authentification d'un utilisateur existant.
 *
 * Un seul endpoint : {@code POST /api/auth/login}. Pour des raisons de
 * sécurité (éviter l'énumération d'emails), on ne distingue pas « email
 * inconnu » de « mot de passe incorrect » : les deux renvoient un 401.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Login", description = "Authentification d'un utilisateur existant")
public class LoginController {

    private final LoginUserUseCase loginUserUseCase;

    public LoginController(LoginUserUseCase loginUserUseCase) {
        this.loginUserUseCase = loginUserUseCase;
    }

    /**
     * Vérifie les identifiants et renvoie un JWT signé.
     *
     * @param request payload validé contenant {@code email} et {@code password}
     * @return un {@link LoginResponse} contenant le JWT
     * @throws com.chatop.back.user.domain.exception.InvalidCredentialsException si l'email
     *         ou le mot de passe ne correspond pas (HTTP 401)
     */
    @PostMapping("/login")
    @SecurityRequirements
    @Operation(summary = "Authentifier un utilisateur et retourner un JWT")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Identifiants valides, JWT émis",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Succès",
                                    value = "{\"token\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQHRlc3QuY29tIiwiaWF0IjoxNzEyMDAwMDAwLCJleHAiOjE3MTIwMDM2MDB9.signature\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload invalide (validation)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Email malformé",
                                    value = "{\"email\": \"must be a well-formed email address\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Email ou mot de passe incorrect",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Identifiants invalides",
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
