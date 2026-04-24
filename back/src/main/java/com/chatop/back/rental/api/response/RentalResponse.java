package com.chatop.back.rental.api.response;

import com.chatop.back.rental.domain.entity.Rental;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Rental representation returned by
 * {@code GET /api/rentals} (in the list) and {@code GET /api/rentals/:id}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalResponse {

    private Long id;
    private String name;
    private BigDecimal surface;
    private BigDecimal price;
    private String picture;
    private String description;

    @JsonProperty("owner_id")
    private Long ownerId;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    public static RentalResponse from(Rental rental) {
        return RentalResponse.builder()
                .id(rental.id())
                .name(rental.name())
                .surface(rental.surface().value())
                .price(rental.price().value())
                .picture(rental.pictureUrl())
                .description(rental.description())
                .ownerId(rental.ownerId())
                .createdAt(rental.createdAt())
                .updatedAt(rental.updatedAt())
                .build();
    }
}
