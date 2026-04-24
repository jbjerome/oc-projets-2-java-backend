package com.chatop.back.rental.application.command;

import com.chatop.back.rental.domain.vo.PictureUpload;

import java.math.BigDecimal;

public record CreateRentalCommand(
        String name,
        BigDecimal surface,
        BigDecimal price,
        String description,
        PictureUpload picture,
        Long ownerId
) {}
