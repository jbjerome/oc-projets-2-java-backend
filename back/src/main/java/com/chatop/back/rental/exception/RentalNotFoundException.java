package com.chatop.back.rental.exception;

public class RentalNotFoundException extends RuntimeException {

    public RentalNotFoundException(Long id) {
        super("Rental not found: " + id);
    }
}
