package com.chatop.back.message.application.usecase;

import com.chatop.back.message.application.command.SendMessageCommand;
import com.chatop.back.message.domain.entity.Message;
import com.chatop.back.message.domain.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SendMessageUseCase {

    private final MessageRepository messageRepository;

    public SendMessageUseCase(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional
    public void handle(SendMessageCommand command) {
        messageRepository.save(Message.create(command.userId(), command.rentalId(), command.content()));
    }
}
