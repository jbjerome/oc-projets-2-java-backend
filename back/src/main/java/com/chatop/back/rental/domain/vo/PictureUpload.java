package com.chatop.back.rental.domain.vo;

import com.chatop.back.rental.domain.exception.InvalidPictureUploadException;

import java.util.Objects;

/**
 * Binary picture payload handed to {@code PictureStorage}.
 *
 * <p>Framework-agnostic on purpose: the domain does not know
 * about {@code MultipartFile}. The API layer adapts the incoming
 * upload into this record before calling the use case.
 */
public record PictureUpload(byte[] bytes, String contentType, String originalFilename) {

    public PictureUpload {
        Objects.requireNonNull(bytes);
        Objects.requireNonNull(contentType);
        Objects.requireNonNull(originalFilename);
        if (bytes.length == 0) {
            throw new InvalidPictureUploadException("Picture payload is empty");
        }
    }
}
