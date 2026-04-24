package com.chatop.back.user.domain.repository;

import com.chatop.back.user.domain.entity.User;

import java.util.Optional;

public interface UserRepository {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    User save(User user);
}
