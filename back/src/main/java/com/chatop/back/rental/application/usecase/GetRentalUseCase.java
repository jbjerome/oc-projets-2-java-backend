package com.chatop.back.rental.application.usecase;

import com.chatop.back.rental.domain.entity.Rental;
import com.chatop.back.rental.domain.exception.RentalNotFoundException;
import com.chatop.back.rental.domain.repository.RentalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetRentalUseCase {

    private final RentalRepository rentalRepository;

    public GetRentalUseCase(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Transactional(readOnly = true)
    public Rental handle(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));
    }
}
