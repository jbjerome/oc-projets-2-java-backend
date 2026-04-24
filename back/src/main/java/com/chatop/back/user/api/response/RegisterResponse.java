package com.chatop.back.user.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSON response returned by {@code POST /api/auth/register} on success.
 *
 * <p>Serialized as {@code { "token": "eyJhbGciOi…" }}.
 * The client must store this value and send it back in the
 * {@code Authorization: Bearer <token>} header to access protected endpoints.
 *
 * <p>The token is HS256-signed with the {@code ${jwt.secret}} secret and
 * expires after {@code ${jwt.expiration-minutes}} minutes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {

    /** Compact serialized JWT (header.payload.signature). */
    private String token;
}
