package com.chatop.back.rental.domain.exception;

public class RentalNotFoundException extends RuntimeException {

    public RentalNotFoundException(Long id) {
        super("Rental not found: " + id);
    }
}
