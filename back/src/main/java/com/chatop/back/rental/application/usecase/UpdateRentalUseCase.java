package com.chatop.back.rental.application.usecase;

import com.chatop.back.rental.application.command.UpdateRentalCommand;
import com.chatop.back.rental.domain.entity.Rental;
import com.chatop.back.rental.domain.exception.NotRentalOwnerException;
import com.chatop.back.rental.domain.exception.RentalNotFoundException;
import com.chatop.back.rental.domain.repository.RentalRepository;
import com.chatop.back.rental.domain.vo.Price;
import com.chatop.back.rental.domain.vo.Surface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateRentalUseCase {

    private final RentalRepository rentalRepository;

    public UpdateRentalUseCase(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Transactional
    public Rental handle(UpdateRentalCommand command) {
        Rental existing = rentalRepository.findById(command.id())
                .orElseThrow(() -> new RentalNotFoundException(command.id()));

        if (!existing.ownerId().equals(command.requesterId())) {
            throw new NotRentalOwnerException(command.id());
        }

        Rental updated = existing.withUpdatedDetails(
                command.name(),
                Surface.of(command.surface()),
                Price.of(command.price()),
                command.description()
        );

        return rentalRepository.save(updated);
    }
}
