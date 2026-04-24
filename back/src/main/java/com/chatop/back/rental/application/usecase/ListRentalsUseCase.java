package com.chatop.back.rental.application.usecase;

import com.chatop.back.rental.domain.entity.Rental;
import com.chatop.back.rental.domain.repository.RentalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListRentalsUseCase {

    private final RentalRepository rentalRepository;

    public ListRentalsUseCase(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Transactional(readOnly = true)
    public List<Rental> handle() {
        return rentalRepository.findAll();
    }
}
