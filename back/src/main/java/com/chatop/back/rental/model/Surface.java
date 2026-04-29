package com.chatop.back.rental.model;

import com.chatop.back.rental.exception.InvalidSurfaceException;

import java.math.BigDecimal;
import java.util.Objects;

public record Surface(BigDecimal value) {

    public Surface {
        Objects.requireNonNull(value);
        if (value.signum() <= 0) {
            throw new InvalidSurfaceException("Surface must be strictly positive");
        }
    }

    public static Surface of(BigDecimal value) {
        return new Surface(value);
    }
}
