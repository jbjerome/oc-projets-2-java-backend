package com.chatop.back.message.domain.entity;

import com.chatop.back.message.domain.exception.InvalidMessageException;

import java.time.Instant;
import java.util.Objects;

public final class Message {

    private static final int MAX_CONTENT_LENGTH = 2000;

    private final Long id;
    private final Long userId;
    private final Long rentalId;
    private final String content;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Message(Long id, Long userId, Long rentalId, String content,
                    Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = requirePositive(userId, "userId");
        this.rentalId = requirePositive(rentalId, "rentalId");
        this.content = requireValidContent(content);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    private static Long requirePositive(Long value, String field) {
        if (value == null || value <= 0) {
            throw new InvalidMessageException(field + " must be strictly positive");
        }
        return value;
    }

    private static String requireValidContent(String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidMessageException("content must not be blank");
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new InvalidMessageException("content must not exceed " + MAX_CONTENT_LENGTH + " characters");
        }
        return content;
    }

    public static Message create(Long userId, Long rentalId, String content) {
        Instant now = Instant.now();
        return new Message(null, userId, rentalId, content, now, now);
    }

    public static Message fromPersistence(Long id, Long userId, Long rentalId, String content,
                                          Instant createdAt, Instant updatedAt) {
        return new Message(id, userId, rentalId, content, createdAt, updatedAt);
    }

    public Long id() { return id; }
    public Long userId() { return userId; }
    public Long rentalId() { return rentalId; }
    public String content() { return content; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }
}
