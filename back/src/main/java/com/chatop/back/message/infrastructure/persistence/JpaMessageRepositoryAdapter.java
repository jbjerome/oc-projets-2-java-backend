package com.chatop.back.message.infrastructure.persistence;

import com.chatop.back.message.domain.entity.Message;
import com.chatop.back.message.domain.repository.MessageRepository;
import org.springframework.stereotype.Component;

@Component
class JpaMessageRepositoryAdapter implements MessageRepository {

    private final SpringDataMessageRepository delegate;

    JpaMessageRepositoryAdapter(SpringDataMessageRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Message save(Message message) {
        JpaMessageEntity entity = toEntity(message);
        JpaMessageEntity saved = delegate.save(entity);
        return toDomain(saved);
    }

    private JpaMessageEntity toEntity(Message message) {
        JpaMessageEntity entity = new JpaMessageEntity();
        entity.setId(message.id());
        entity.setUserId(message.userId());
        entity.setRentalId(message.rentalId());
        entity.setMessage(message.content());
        entity.setCreatedAt(message.createdAt());
        entity.setUpdatedAt(message.updatedAt());
        return entity;
    }

    private Message toDomain(JpaMessageEntity entity) {
        return Message.fromPersistence(
                entity.getId(),
                entity.getUserId(),
                entity.getRentalId(),
                entity.getMessage(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
