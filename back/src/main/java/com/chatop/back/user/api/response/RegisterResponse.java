package com.chatop.back.user.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Réponse JSON renvoyée par {@code POST /api/auth/register} après succès.
 *
 * <p>Sérialisée sous la forme {@code { "token": "eyJhbGciOi…" }}.
 * Le client doit stocker cette valeur et la renvoyer ensuite dans l'en-tête
 * {@code Authorization: Bearer <token>} pour accéder aux endpoints protégés.
 *
 * <p>Le token est signé HS256 avec le secret {@code ${jwt.secret}} et expire
 * après {@code ${jwt.expiration-minutes}} minutes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {

    /** JWT compact sérialisé (header.payload.signature). */
    private String token;
}
