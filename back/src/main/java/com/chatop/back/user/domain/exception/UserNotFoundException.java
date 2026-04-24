package com.chatop.back.user.domain.exception;

/**
 * Thrown when a referenced user (by email or id) does not exist in the database.
 *
 * <p>Main use case: during {@code GET /api/auth/me} if the account associated
 * with the JWT has been deleted between token issuance and its use.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String identifier) {
        super("User not found: " + identifier);
    }
}
