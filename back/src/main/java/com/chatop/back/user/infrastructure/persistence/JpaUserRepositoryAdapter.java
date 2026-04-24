package com.chatop.back.user.infrastructure.persistence;

import com.chatop.back.user.domain.entity.User;
import com.chatop.back.user.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class JpaUserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository delegate;

    JpaUserRepositoryAdapter(SpringDataUserRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean existsByEmail(String email) {
        return delegate.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return delegate.findByEmail(email).map(this::toDomain);
    }

    @Override
    public User save(User user) {
        JpaUserEntity entity = toEntity(user);
        JpaUserEntity saved = delegate.save(entity);
        return toDomain(saved);
    }

    private JpaUserEntity toEntity(User user) {
        JpaUserEntity entity = new JpaUserEntity();
        entity.setId(user.id());
        entity.setName(user.name());
        entity.setEmail(user.email());
        entity.setPassword(user.passwordHash());
        entity.setCreatedAt(user.createdAt());
        entity.setUpdatedAt(user.updatedAt());
        return entity;
    }

    private User toDomain(JpaUserEntity entity) {
        return User.fromPersistence(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
