package com.chatop.back.user.domain;

/**
 * Levée quand un utilisateur référencé (par email ou id) n'existe pas en base.
 *
 * <p>Cas d'usage principal : lors de {@code GET /api/auth/me} si le compte
 * associé au JWT a été supprimé entre l'émission du token et son utilisation.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String identifier) {
        super("User not found: " + identifier);
    }
}
