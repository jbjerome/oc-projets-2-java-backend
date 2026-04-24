package com.chatop.back.user.api;

import com.chatop.back.user.api.request.RegisterRequest;
import com.chatop.back.user.api.response.RegisterResponse;
import com.chatop.back.user.application.RegisterUserCommand;
import com.chatop.back.user.application.RegisterUserUseCase;
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
 * Contrôleur REST dédié à l'inscription d'un nouvel utilisateur.
 *
 * <p>Un seul endpoint : {@code POST /api/auth/register}. Login et profil
 * courant sont gérés dans des contrôleurs séparés.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Register", description = "Inscription d'un nouvel utilisateur")
public class RegisterController {

    private final RegisterUserUseCase registerUserUseCase;

    public RegisterController(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    /**
     * Crée un nouveau compte utilisateur et renvoie un JWT signé.
     *
     * @param request payload validé contenant {@code name}, {@code email} et {@code password}
     * @return un {@link RegisterResponse} contenant le JWT à utiliser en header {@code Authorization} des requêtes
     * @throws com.chatop.back.user.domain.EmailAlreadyUsedException si email déjà utilisé (HTTP 400)
     */
    @PostMapping("/register")
    @SecurityRequirements
    @Operation(summary = "Créer un compte utilisateur et retourner un JWT")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Compte créé, JWT émis",
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
                    description = "Payload invalide (validation) ou email déjà utilisé",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Email malformé",
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
