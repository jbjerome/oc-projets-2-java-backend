package com.chatop.back.rental.domain.exception;

public class InvalidPictureUploadException extends IllegalArgumentException {

    public InvalidPictureUploadException(String message) {
        super(message);
    }
}
