package com.chatop.back.rental.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "RENTALS")
class JpaRentalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal surface;

    private BigDecimal price;

    private String picture;

    private String description;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    protected JpaRentalEntity() {}

    Long getId() { return id; }
    void setId(Long id) { this.id = id; }

    String getName() { return name; }
    void setName(String name) { this.name = name; }

    BigDecimal getSurface() { return surface; }
    void setSurface(BigDecimal surface) { this.surface = surface; }

    BigDecimal getPrice() { return price; }
    void setPrice(BigDecimal price) { this.price = price; }

    String getPicture() { return picture; }
    void setPicture(String picture) { this.picture = picture; }

    String getDescription() { return description; }
    void setDescription(String description) { this.description = description; }

    Long getOwnerId() { return ownerId; }
    void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    Instant getCreatedAt() { return createdAt; }
    void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    Instant getUpdatedAt() { return updatedAt; }
    void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
