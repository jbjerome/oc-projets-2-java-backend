package com.chatop.back.rental.service;

import com.chatop.back.rental.dto.UpdateRentalRequest;
import com.chatop.back.rental.exception.NotRentalOwnerException;
import com.chatop.back.rental.exception.RentalNotFoundException;
import com.chatop.back.rental.model.Price;
import com.chatop.back.rental.model.Rental;
import com.chatop.back.rental.model.Surface;
import com.chatop.back.rental.repository.RentalRepository;
import com.chatop.back.rental.storage.PictureStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock RentalRepository rentalRepository;
    @Mock PictureStorage pictureStorage;

    @InjectMocks RentalService rentalService;

    @Test
    void getById_throws_when_missing() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rentalService.getById(1L))
                .isInstanceOf(RentalNotFoundException.class);
    }

    @Test
    void update_throws_when_not_owner() {
        Rental existing = Rental.builder()
                .id(5L).ownerId(42L)
                .surface(Surface.of(BigDecimal.TEN)).price(Price.of(BigDecimal.TEN))
                .build();
        when(rentalRepository.findById(5L)).thenReturn(Optional.of(existing));

        UpdateRentalRequest request = UpdateRentalRequest.builder()
                .name("x").surface(BigDecimal.ONE).price(BigDecimal.ONE).description("d").build();

        assertThatThrownBy(() -> rentalService.update(5L, request, 99L))
                .isInstanceOf(NotRentalOwnerException.class);

        verify(rentalRepository, never()).save(any());
    }
}
