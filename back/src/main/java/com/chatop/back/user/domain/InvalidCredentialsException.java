package com.chatop.back.user.domain;

/**
 * Levée par {@code LoginUserUseCase} quand l'email est inconnu
 * OU que le mot de passe ne correspond pas.
 *
 * <p>Le message est volontairement générique : ne pas distinguer les deux
 * cas évite la fuite d'information (énumération d'emails).
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid credentials");
    }
}
