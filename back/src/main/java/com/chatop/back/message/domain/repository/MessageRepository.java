package com.chatop.back.message.domain.repository;

import com.chatop.back.message.domain.entity.Message;

public interface MessageRepository {

    Message save(Message message);
}
