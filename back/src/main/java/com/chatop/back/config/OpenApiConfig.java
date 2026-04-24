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
 * Swagger / OpenAPI configuration.
 *
 * <p>Declares the {@code bearerAuth} security scheme (JWT), applies it to
 * all routes by default, and automatically injects a 401 response on every
 * operation that requires a JWT.
 * Public endpoints must opt-out via {@code @SecurityRequirements}.
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
                        .description("Rental property API"))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    /**
     * Adds a 401 response to every operation that is not explicitly
     * marked as public (security non-empty or null).
     */
    @Bean
    OpenApiCustomizer addUnauthorizedResponse() {
        ApiResponse unauthorized = new ApiResponse()
                .description("Unauthorized — missing, expired or invalid JWT");

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
