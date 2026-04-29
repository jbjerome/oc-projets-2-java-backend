package com.chatop.back.message.service;

import com.chatop.back.message.dto.CreateMessageRequest;
import com.chatop.back.message.model.Message;
import com.chatop.back.message.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional
    public void send(CreateMessageRequest request) {
        Message message = Message.builder()
                .userId(request.getUserId())
                .rentalId(request.getRentalId())
                .message(request.getMessage())
                .build();
        messageRepository.save(message);
    }
}
