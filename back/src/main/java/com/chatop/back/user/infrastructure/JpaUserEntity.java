package com.chatop.back.user.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "USERS")
class JpaUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;

    private String password;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    protected JpaUserEntity() {}

    Long getId() { return id; }
    void setId(Long id) { this.id = id; }

    String getEmail() { return email; }
    void setEmail(String email) { this.email = email; }

    String getName() { return name; }
    void setName(String name) { this.name = name; }

    String getPassword() { return password; }
    void setPassword(String password) { this.password = password; }

    Instant getCreatedAt() { return createdAt; }
    void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    Instant getUpdatedAt() { return updatedAt; }
    void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
