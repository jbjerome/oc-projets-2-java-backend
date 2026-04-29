package com.chatop.back.rental.repository;

import com.chatop.back.rental.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
}
