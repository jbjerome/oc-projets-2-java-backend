package com.chatop.back.rental.exception;

public class NotRentalOwnerException extends RuntimeException {

    public NotRentalOwnerException(Long rentalId) {
        super("Not the owner of rental: " + rentalId);
    }
}
