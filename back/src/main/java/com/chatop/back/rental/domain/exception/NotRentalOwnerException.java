package com.chatop.back.rental.domain.exception;

public class NotRentalOwnerException extends RuntimeException {

    public NotRentalOwnerException(Long rentalId) {
        super("Not the owner of rental: " + rentalId);
    }
}
