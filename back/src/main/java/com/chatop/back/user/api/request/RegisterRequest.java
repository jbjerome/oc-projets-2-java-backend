package com.chatop.back.user.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload JSON reçu sur {@code POST /api/auth/register}.
 *
 * <p>Validé automatiquement via {@code @Valid} sur le paramètre du contrôleur.
 * En cas de violation, Spring lève {@code MethodArgumentNotValidException}
 * et le {@code GlobalExceptionHandler} répond HTTP 400 avec la liste des
 * erreurs sous la forme {@code { "champ": "message" }}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    /** Nom d'affichage — obligatoire, non vide. */
    @NotBlank
    private String name;

    /** Email valide — obligatoire, sert d'identifiant de connexion (unique en base). */
    @NotBlank
    @Email
    private String email;

    /** Mot de passe en clair — obligatoire, 6 à 100 caractères, hashé (BCrypt) avant stockage. */
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
}
