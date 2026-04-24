package com.chatop.back.rental.infrastructure.persistence;

import com.chatop.back.rental.domain.entity.Rental;
import com.chatop.back.rental.domain.repository.RentalRepository;
import com.chatop.back.rental.domain.vo.Price;
import com.chatop.back.rental.domain.vo.Surface;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class JpaRentalRepositoryAdapter implements RentalRepository {

    private final SpringDataRentalRepository delegate;

    JpaRentalRepositoryAdapter(SpringDataRentalRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<Rental> findAll() {
        return delegate.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Rental> findById(Long id) {
        return delegate.findById(id).map(this::toDomain);
    }

    @Override
    public Rental save(Rental rental) {
        JpaRentalEntity entity = rental.id() == null
                ? new JpaRentalEntity()
                : delegate.findById(rental.id()).orElseGet(JpaRentalEntity::new);
        apply(rental, entity);
        JpaRentalEntity saved = delegate.save(entity);
        return toDomain(saved);
    }

    private void apply(Rental rental, JpaRentalEntity entity) {
        entity.setName(rental.name());
        entity.setSurface(rental.surface().value());
        entity.setPrice(rental.price().value());
        entity.setPicture(rental.pictureUrl());
        entity.setDescription(rental.description());
        entity.setOwnerId(rental.ownerId());
        entity.setCreatedAt(rental.createdAt());
        entity.setUpdatedAt(rental.updatedAt());
    }

    private Rental toDomain(JpaRentalEntity entity) {
        return Rental.fromPersistence(
                entity.getId(),
                entity.getName(),
                Surface.of(entity.getSurface()),
                Price.of(entity.getPrice()),
                entity.getPicture(),
                entity.getDescription(),
                entity.getOwnerId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
