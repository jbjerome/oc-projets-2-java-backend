package com.chatop.back.rental.domain.exception;

public class InvalidPriceException extends IllegalArgumentException {

    public InvalidPriceException(String message) {
        super(message);
    }
}
