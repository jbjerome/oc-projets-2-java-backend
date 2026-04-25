package com.chatop.back.message.domain.exception;

public class InvalidMessageException extends IllegalArgumentException {

    public InvalidMessageException(String message) {
        super(message);
    }
}
