package com.chatop.back.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface SpringDataUserRepository extends JpaRepository<JpaUserEntity, Long> {

    boolean existsByEmail(String email);

    Optional<JpaUserEntity> findByEmail(String email);
}
