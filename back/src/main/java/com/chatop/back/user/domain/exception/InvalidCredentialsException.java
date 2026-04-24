package com.chatop.back.user.domain.exception;

/**
 * Thrown by {@code LoginUserUseCase} when the email is unknown
 * OR the password does not match.
 *
 * <p>The message is intentionally generic: not distinguishing the two cases
 * prevents information leakage (email enumeration).
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid credentials");
    }
}
