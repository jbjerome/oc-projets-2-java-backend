package com.chatop.back.message.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "MESSAGES")
class JpaMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "rental_id")
    private Long rentalId;

    private String message;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    protected JpaMessageEntity() {}

    Long getId() { return id; }
    void setId(Long id) { this.id = id; }

    Long getUserId() { return userId; }
    void setUserId(Long userId) { this.userId = userId; }

    Long getRentalId() { return rentalId; }
    void setRentalId(Long rentalId) { this.rentalId = rentalId; }

    String getMessage() { return message; }
    void setMessage(String message) { this.message = message; }

    Instant getCreatedAt() { return createdAt; }
    void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    Instant getUpdatedAt() { return updatedAt; }
    void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
