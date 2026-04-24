package com.chatop.back.user.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Réponse JSON renvoyée par {@code POST /api/auth/login} après succès.
 *
 * <p>Sérialisée sous la forme {@code { "token": "eyJhbGciOi…" }}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    /** JWT compact sérialisé (header.payload.signature). */
    private String token;
}
