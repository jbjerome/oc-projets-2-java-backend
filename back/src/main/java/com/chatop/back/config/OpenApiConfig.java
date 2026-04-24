package com.chatop.back.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Swagger / OpenAPI.
 *
 * <p>Déclare le schéma de sécurité {@code bearerAuth} (JWT), l'applique à
 * toutes les routes par défaut, et injecte automatiquement une réponse 401
 * sur toute opération qui requiert un JWT.
 * Les endpoints publics doivent opter-out via
 * {@code @SecurityRequirements}.
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME = "bearerAuth";

    @Bean
    OpenAPI chatopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ChâTop API")
                        .version("v1")
                        .description("API de location immobilière"))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    /**
     * Ajoute une réponse 401 sur toutes les opérations qui ne sont pas
     * explicitement marquées comme publiques (security non-vide ou null).
     */
    @Bean
    OpenApiCustomizer addUnauthorizedResponse() {
        ApiResponse unauthorized = new ApiResponse()
                .description("Unauthorized — JWT manquant, expiré ou invalide");

        return openApi -> openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(op -> {
                    boolean isPublic = op.getSecurity() != null && op.getSecurity().isEmpty();
                    if (!isPublic) {
                        op.getResponses().addApiResponse("401", unauthorized);
                    }
                })
        );
    }
}
