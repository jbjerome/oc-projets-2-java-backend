package com.chatop.back.message.repository;

import com.chatop.back.message.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
