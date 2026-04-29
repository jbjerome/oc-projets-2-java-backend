package com.chatop.back.config;

import com.chatop.back.message.exception.InvalidMessageException;
import com.chatop.back.rental.exception.InvalidPictureUploadException;
import com.chatop.back.rental.exception.InvalidPriceException;
import com.chatop.back.rental.exception.InvalidSurfaceException;
import com.chatop.back.rental.exception.NotRentalOwnerException;
import com.chatop.back.rental.exception.RentalNotFoundException;
import com.chatop.back.user.exception.EmailAlreadyUsedException;
import com.chatop.back.user.exception.InvalidCredentialsException;
import com.chatop.back.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> onValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<Map<String, String>> onEmailInUse(EmailAlreadyUsedException ex) {
        return ResponseEntity.badRequest().body(Map.of("email", ex.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> onInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "error"));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> onUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(RentalNotFoundException.class)
    public ResponseEntity<Map<String, String>> onRentalNotFound(RentalNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(NotRentalOwnerException.class)
    public ResponseEntity<Map<String, String>> onNotRentalOwner(NotRentalOwnerException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(InvalidPriceException.class)
    public ResponseEntity<Map<String, String>> onInvalidPrice(InvalidPriceException ex) {
        return ResponseEntity.badRequest().body(Map.of("price", ex.getMessage()));
    }

    @ExceptionHandler(InvalidSurfaceException.class)
    public ResponseEntity<Map<String, String>> onInvalidSurface(InvalidSurfaceException ex) {
        return ResponseEntity.badRequest().body(Map.of("surface", ex.getMessage()));
    }

    @ExceptionHandler(InvalidPictureUploadException.class)
    public ResponseEntity<Map<String, String>> onInvalidPicture(InvalidPictureUploadException ex) {
        return ResponseEntity.badRequest().body(Map.of("picture", ex.getMessage()));
    }

    @ExceptionHandler(InvalidMessageException.class)
    public ResponseEntity<Map<String, String>> onInvalidMessage(InvalidMessageException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}
