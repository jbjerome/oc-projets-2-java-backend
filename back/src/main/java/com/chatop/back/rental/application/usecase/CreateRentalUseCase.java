package com.chatop.back.rental.application.usecase;

import com.chatop.back.rental.application.command.CreateRentalCommand;
import com.chatop.back.rental.domain.entity.Rental;
import com.chatop.back.rental.domain.repository.RentalRepository;
import com.chatop.back.rental.domain.service.PictureStorage;
import com.chatop.back.rental.domain.vo.Price;
import com.chatop.back.rental.domain.vo.Surface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateRentalUseCase {

    private final RentalRepository rentalRepository;
    private final PictureStorage pictureStorage;

    public CreateRentalUseCase(RentalRepository rentalRepository,
                               PictureStorage pictureStorage) {
        this.rentalRepository = rentalRepository;
        this.pictureStorage = pictureStorage;
    }

    @Transactional
    public Rental handle(CreateRentalCommand command) {
        String pictureUrl = pictureStorage.store(command.picture());

        Rental rental = Rental.create(
                command.name(),
                Surface.of(command.surface()),
                Price.of(command.price()),
                pictureUrl,
                command.description(),
                command.ownerId()
        );

        return rentalRepository.save(rental);
    }
}
