package com.chatop.back.rental.service;

import com.chatop.back.rental.dto.CreateRentalRequest;
import com.chatop.back.rental.dto.UpdateRentalRequest;
import com.chatop.back.rental.exception.NotRentalOwnerException;
import com.chatop.back.rental.exception.RentalNotFoundException;
import com.chatop.back.rental.model.Price;
import com.chatop.back.rental.model.Rental;
import com.chatop.back.rental.model.Surface;
import com.chatop.back.rental.repository.RentalRepository;
import com.chatop.back.rental.storage.PictureStorage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final PictureStorage pictureStorage;

    public RentalService(RentalRepository rentalRepository,
                         PictureStorage pictureStorage) {
        this.rentalRepository = rentalRepository;
        this.pictureStorage = pictureStorage;
    }

    @Transactional(readOnly = true)
    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Rental getById(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));
    }

    @Transactional
    public Rental create(CreateRentalRequest request, Long ownerId) {
        String pictureUrl = pictureStorage.store(request.getPicture());
        Rental rental = Rental.builder()
                .name(request.getName())
                .surface(Surface.of(request.getSurface()))
                .price(Price.of(request.getPrice()))
                .picture(pictureUrl)
                .description(request.getDescription())
                .ownerId(ownerId)
                .build();
        return rentalRepository.save(rental);
    }

    @Transactional
    public Rental update(Long id, UpdateRentalRequest request, Long requesterId) {
        Rental existing = getById(id);
        if (!existing.getOwnerId().equals(requesterId)) {
            throw new NotRentalOwnerException(id);
        }
        existing.setName(request.getName());
        existing.setSurface(Surface.of(request.getSurface()));
        existing.setPrice(Price.of(request.getPrice()));
        existing.setDescription(request.getDescription());
        return rentalRepository.save(existing);
    }
}
