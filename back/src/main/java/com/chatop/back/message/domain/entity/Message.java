package com.chatop.back.message.domain.entity;

import java.time.Instant;
import java.util.Objects;

public final class Message {

    private final Long id;
    private final Long userId;
    private final Long rentalId;
    private final String content;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Message(Long id, Long userId, Long rentalId, String content,
                    Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId);
        this.rentalId = Objects.requireNonNull(rentalId);
        this.content = Objects.requireNonNull(content);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
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
