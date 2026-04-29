package com.chatop.back.rental.exception;

public class InvalidPriceException extends IllegalArgumentException {

    public InvalidPriceException(String message) {
        super(message);
    }
}
