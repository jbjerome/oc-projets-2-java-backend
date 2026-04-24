package com.chatop.back.rental.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic message envelope used by {@code POST /api/rentals}
 * and {@code PUT /api/rentals/:id}: {@code { "message": "..." }}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {

    private String message;
}
