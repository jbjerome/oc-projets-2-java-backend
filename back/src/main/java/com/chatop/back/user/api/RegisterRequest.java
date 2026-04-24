package com.chatop.back.user.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload JSON reçu sur {@code POST /api/auth/register}.
 *
 * <p>Validé automatiquement via {@code @Valid} sur le paramètre du contrôleur.
 * En cas de violation d'une contrainte, Spring lève une
 * {@code MethodArgumentNotValidException} et le {@code GlobalExceptionHandler}
 * répond HTTP 400 avec un objet {@code { "champ": "message" }} listant les
 * erreurs.
 *
 * @param name     nom d'affichage de l'utilisateur — obligatoire, non vide
 * @param email    adresse email de l'utilisateur — obligatoire, format RFC 5322,
 *                 sert également d'identifiant de connexion (unique en base)
 * @param password mot de passe en clair — obligatoire, entre 6 et 100 caractères,
 *                 hashé (BCrypt) côté serveur avant stockage
 */
public record RegisterRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, max = 100) String password
) {}
