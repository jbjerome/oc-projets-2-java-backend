package com.chatop.back.rental.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * {@code multipart/form-data} payload received on {@code POST /api/rentals}.
 *
 * <p>Bound via {@code @ModelAttribute} on the controller (not {@code @RequestBody}
 * since this is multipart). The {@code picture} field is the image uploaded
 * by the user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRentalRequest {

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

    @NotNull
    private MultipartFile picture;
}
