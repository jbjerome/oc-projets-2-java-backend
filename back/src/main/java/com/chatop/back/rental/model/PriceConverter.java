package com.chatop.back.rental.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

@Converter(autoApply = true)
public class PriceConverter implements AttributeConverter<Price, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Price attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public Price convertToEntityAttribute(BigDecimal dbData) {
        return dbData == null ? null : Price.of(dbData);
    }
}
