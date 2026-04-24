package com.chatop.back.rental.domain.vo;

import com.chatop.back.rental.domain.exception.InvalidPriceException;

import java.math.BigDecimal;
import java.util.Objects;

public record Price(BigDecimal value) {

    public Price {
        Objects.requireNonNull(value);
        if (value.signum() <= 0) {
            throw new InvalidPriceException("Price must be strictly positive");
        }
    }

    public static Price of(BigDecimal value) {
        return new Price(value);
    }
}
