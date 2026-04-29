package com.chatop.back.rental.dto;

import com.chatop.back.rental.model.Rental;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

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
                .id(rental.getId())
                .name(rental.getName())
                .surface(rental.getSurface().value())
                .price(rental.getPrice().value())
                .picture(rental.getPicture())
                .description(rental.getDescription())
                .ownerId(rental.getOwnerId())
                .createdAt(rental.getCreatedAt())
                .updatedAt(rental.getUpdatedAt())
                .build();
    }
}
