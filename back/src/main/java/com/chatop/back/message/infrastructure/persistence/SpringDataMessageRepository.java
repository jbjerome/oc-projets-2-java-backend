package com.chatop.back.message.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataMessageRepository extends JpaRepository<JpaMessageEntity, Long> {
}
