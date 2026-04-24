package com.chatop.back.rental.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Payload {@code multipart/form-data} reçu sur {@code PUT /api/rentals/:id}.
 *
 * <p>L'image ({@code picture}) n'est pas modifiable via cet endpoint
 * conformément à la définition de l'API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRentalRequest {

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private BigDecimal surface;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotBlank
    private String description;
}
