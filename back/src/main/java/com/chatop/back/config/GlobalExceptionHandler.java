package com.chatop.back.config;

import com.chatop.back.message.domain.exception.InvalidMessageException;
import com.chatop.back.rental.domain.exception.InvalidPictureUploadException;
import com.chatop.back.rental.domain.exception.InvalidPriceException;
import com.chatop.back.rental.domain.exception.InvalidSurfaceException;
import com.chatop.back.rental.domain.exception.NotRentalOwnerException;
import com.chatop.back.rental.domain.exception.RentalNotFoundException;
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
 * Translates business exceptions into HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 400 — validation constraint violation on a DTO. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> onValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    /** 400 — email already used at registration. */
    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<Map<String, String>> onEmailInUse(EmailAlreadyUsedException ex) {
        return ResponseEntity.badRequest().body(Map.of("email", ex.getMessage()));
    }

    /** 401 — login failed (unknown email or wrong password). */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> onInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "error"));
    }

    /** 404 — referenced user (typically via the JWT) does not exist. */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> onUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
    }

    /** 404 — rental id does not exist. */
    @ExceptionHandler(RentalNotFoundException.class)
    public ResponseEntity<Map<String, String>> onRentalNotFound(RentalNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
    }

    /** 403 — the authenticated user is not the owner of the targeted rental. */
    @ExceptionHandler(NotRentalOwnerException.class)
    public ResponseEntity<Map<String, String>> onNotRentalOwner(NotRentalOwnerException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", ex.getMessage()));
    }

    /** 400 — invalid price value (negative, null). */
    @ExceptionHandler(InvalidPriceException.class)
    public ResponseEntity<Map<String, String>> onInvalidPrice(InvalidPriceException ex) {
        return ResponseEntity.badRequest().body(Map.of("price", ex.getMessage()));
    }

    /** 400 — invalid surface value (negative, null). */
    @ExceptionHandler(InvalidSurfaceException.class)
    public ResponseEntity<Map<String, String>> onInvalidSurface(InvalidSurfaceException ex) {
        return ResponseEntity.badRequest().body(Map.of("surface", ex.getMessage()));
    }

    /** 400 — invalid uploaded picture (missing, wrong type, too large). */
    @ExceptionHandler(InvalidPictureUploadException.class)
    public ResponseEntity<Map<String, String>> onInvalidPicture(InvalidPictureUploadException ex) {
        return ResponseEntity.badRequest().body(Map.of("picture", ex.getMessage()));
    }

    /** 400 — invalid message (blank content, exceeds max length, non-positive ids). */
    @ExceptionHandler(InvalidMessageException.class)
    public ResponseEntity<Map<String, String>> onInvalidMessage(InvalidMessageException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}
