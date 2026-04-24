package com.chatop.back.user.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSON response returned by {@code POST /api/auth/login} on success.
 *
 * <p>Serialized as {@code { "token": "eyJhbGciOi…" }}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    /** Compact serialized JWT (header.payload.signature). */
    private String token;
}
