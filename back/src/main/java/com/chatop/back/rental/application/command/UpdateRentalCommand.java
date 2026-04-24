package com.chatop.back.rental.application.command;

import java.math.BigDecimal;

public record UpdateRentalCommand(
        Long id,
        String name,
        BigDecimal surface,
        BigDecimal price,
        String description,
        Long requesterId
) {}
