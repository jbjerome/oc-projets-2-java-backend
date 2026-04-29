package com.chatop.back.rental.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

@Converter(autoApply = true)
public class SurfaceConverter implements AttributeConverter<Surface, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Surface attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public Surface convertToEntityAttribute(BigDecimal dbData) {
        return dbData == null ? null : Surface.of(dbData);
    }
}
