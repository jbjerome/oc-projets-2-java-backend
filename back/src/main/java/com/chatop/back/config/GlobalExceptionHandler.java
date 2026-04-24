package com.chatop.back.config;

import com.chatop.back.user.domain.exception.EmailAlreadyUsedException;
import com.chatop.back.user.domain.exception.InvalidCredentialsException;
import com.chatop.back.user.domain.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Traduction des exceptions métier en réponses HTTP.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 400 — violation des contraintes de validation sur un DTO. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> onValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    /** 400 — email déjà utilisé à l'inscription. */
    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<Map<String, String>> onEmailInUse(EmailAlreadyUsedException ex) {
        return ResponseEntity.badRequest().body(Map.of("email", ex.getMessage()));
    }

    /** 401 — login raté (email inconnu ou mot de passe faux). */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> onInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "error"));
    }

    /** 404 — utilisateur référencé (typiquement par le JWT) inexistant. */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> onUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
    }
}
