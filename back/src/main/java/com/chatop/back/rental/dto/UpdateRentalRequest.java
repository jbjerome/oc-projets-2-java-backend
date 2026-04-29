package com.chatop.back.rental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
