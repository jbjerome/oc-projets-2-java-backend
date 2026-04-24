package com.chatop.back.rental.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Envelope returned by {@code GET /api/rentals}:
 * {@code { "rentals": [ ... ] }}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalsResponse {

    private List<RentalResponse> rentals;
}
