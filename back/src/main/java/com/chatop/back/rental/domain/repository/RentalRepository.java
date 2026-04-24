package com.chatop.back.rental.domain.repository;

import com.chatop.back.rental.domain.entity.Rental;

import java.util.List;
import java.util.Optional;

public interface RentalRepository {

    List<Rental> findAll();

    Optional<Rental> findById(Long id);

    Rental save(Rental rental);
}
