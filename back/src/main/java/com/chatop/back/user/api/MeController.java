package com.chatop.back.user.api;

import com.chatop.back.user.api.response.UserResponse;
import com.chatop.back.user.application.GetCurrentUserUseCase;
import com.chatop.back.user.domain.User;
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
 * Contrôleur REST renvoyant les informations de l'utilisateur connecté.
 *
 * <p>L'identité est lue depuis le claim {@code sub} du JWT injecté par
 * Spring Security via {@code @AuthenticationPrincipal}. Aucune recherche
 * par ID ni paramètre n'est exposé — on ne peut voir que son propre profil.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Me", description = "Informations sur l'utilisateur connecté")
public class MeController {

    private final GetCurrentUserUseCase getCurrentUserUseCase;

    public MeController(GetCurrentUserUseCase getCurrentUserUseCase) {
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }

    /**
     * Renvoie le profil de l'utilisateur courant.
     *
     * @param jwt JWT validé injecté par Spring Security
     * @return un {@link UserResponse} sans le mot de passe hashé
     * @throws com.chatop.back.user.domain.UserNotFoundException si le compte
     *         associé au JWT n'existe plus en base (HTTP 404)
     */
    @GetMapping("/me")
    @Operation(summary = "Récupérer le profil de l'utilisateur connecté")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Profil de l'utilisateur",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Succès",
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
