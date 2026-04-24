package com.chatop.back.rental.domain.entity;

import com.chatop.back.rental.domain.vo.Price;
import com.chatop.back.rental.domain.vo.Surface;

import java.time.Instant;
import java.util.Objects;

public final class Rental {

    private final Long id;
    private final String name;
    private final Surface surface;
    private final Price price;
    private final String pictureUrl;
    private final String description;
    private final Long ownerId;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Rental(Long id, String name, Surface surface, Price price, String pictureUrl,
                   String description, Long ownerId, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.surface = Objects.requireNonNull(surface);
        this.price = Objects.requireNonNull(price);
        this.pictureUrl = Objects.requireNonNull(pictureUrl);
        this.description = Objects.requireNonNull(description);
        this.ownerId = Objects.requireNonNull(ownerId);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public static Rental create(String name, Surface surface, Price price, String pictureUrl,
                                String description, Long ownerId) {
        Instant now = Instant.now();
        return new Rental(null, name, surface, price, pictureUrl, description, ownerId, now, now);
    }

    public static Rental fromPersistence(Long id, String name, Surface surface, Price price,
                                         String pictureUrl, String description, Long ownerId,
                                         Instant createdAt, Instant updatedAt) {
        return new Rental(id, name, surface, price, pictureUrl, description, ownerId, createdAt, updatedAt);
    }

    public Rental withUpdatedDetails(String name, Surface surface, Price price, String description) {
        return new Rental(this.id, name, surface, price, this.pictureUrl, description,
                this.ownerId, this.createdAt, Instant.now());
    }

    public Long id() { return id; }
    public String name() { return name; }
    public Surface surface() { return surface; }
    public Price price() { return price; }
    public String pictureUrl() { return pictureUrl; }
    public String description() { return description; }
    public Long ownerId() { return ownerId; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }
}
