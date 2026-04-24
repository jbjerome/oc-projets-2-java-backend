package com.chatop.back.user.api;

/**
 * Réponse JSON contenant un JWT émis après une inscription ou une connexion
 * réussie.
 *
 * <p>Sérialisée sous la forme {@code { "token": "eyJhbGciOi…" }}. Le client
 * doit stocker cette valeur et la renvoyer ensuite dans l'en-tête
 * {@code Authorization: Bearer <token>} pour accéder aux endpoints protégés.
 *
 * <p>Le token est signé en HS256 avec le secret {@code ${jwt.secret}} et
 * expire après {@code ${jwt.expiration-minutes}} minutes (voir
 * {@code application.properties}).
 *
 * @param token JWT compact sérialisé (header.payload.signature)
 */
public record TokenResponse(String token) {}
