package com.chatop.back.user.domain;

import java.time.Instant;
import java.util.Objects;

public final class User {

    private final Long id;
    private final String name;
    private final String email;
    private final String passwordHash;
    private final Instant createdAt;
    private final Instant updatedAt;

    private User(Long id, String name, String email, String passwordHash,
                 Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.email = Objects.requireNonNull(email);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public static User create(String name, String email, String passwordHash) {
        Instant now = Instant.now();
        return new User(null, name, email, passwordHash, now, now);
    }

    public static User fromPersistence(Long id, String name, String email, String passwordHash,
                                       Instant createdAt, Instant updatedAt) {
        return new User(id, name, email, passwordHash, createdAt, updatedAt);
    }

    public Long id() { return id; }
    public String name() { return name; }
    public String email() { return email; }
    public String passwordHash() { return passwordHash; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }
}
