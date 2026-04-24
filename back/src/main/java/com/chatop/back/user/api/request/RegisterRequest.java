package com.chatop.back.user.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSON payload received on {@code POST /api/auth/register}.
 *
 * <p>Validated automatically via {@code @Valid} on the controller parameter.
 * On violation, Spring throws {@code MethodArgumentNotValidException} and
 * {@code GlobalExceptionHandler} returns HTTP 400 with the list of errors
 * as {@code { "field": "message" }}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    /** Display name — required, non-blank. */
    @NotBlank
    private String name;

    /** Valid email — required, serves as the login identifier (unique in DB). */
    @NotBlank
    @Email
    private String email;

    /** Plain-text password — required, 6 to 100 characters, hashed (BCrypt) before storage. */
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
}
