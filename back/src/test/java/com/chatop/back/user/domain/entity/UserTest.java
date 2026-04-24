package com.chatop.back.user.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    void create_new_user_assigns_null_id_and_timestamps() {
        User user = User.create("John", "john@test.com", "hashed");

        assertThat(user.id()).isNull();
        assertThat(user.name()).isEqualTo("John");
        assertThat(user.email()).isEqualTo("john@test.com");
        assertThat(user.passwordHash()).isEqualTo("hashed");
        assertThat(user.createdAt()).isNotNull();
        assertThat(user.updatedAt()).isEqualTo(user.createdAt());
    }

    @Test
    void rehydrate_preserves_all_fields() {
        Instant created = Instant.parse("2024-01-15T10:00:00Z");
        Instant updated = Instant.parse("2024-02-20T12:00:00Z");

        User user = User.fromPersistence(42L, "Jane", "jane@test.com", "hash", created, updated);

        assertThat(user.id()).isEqualTo(42L);
        assertThat(user.name()).isEqualTo("Jane");
        assertThat(user.email()).isEqualTo("jane@test.com");
        assertThat(user.passwordHash()).isEqualTo("hash");
        assertThat(user.createdAt()).isEqualTo(created);
        assertThat(user.updatedAt()).isEqualTo(updated);
    }

    @Test
    void create_rejects_null_name() {
        assertThatThrownBy(() -> User.create(null, "a@b.c", "hash"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void create_rejects_null_email() {
        assertThatThrownBy(() -> User.create("John", null, "hash"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void create_rejects_null_password_hash() {
        assertThatThrownBy(() -> User.create("John", "a@b.c", null))
                .isInstanceOf(NullPointerException.class);
    }
}
