package com.tamisa.superadmin.Converter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;

public abstract class JsonAttributeConverter<T> implements AttributeConverter<List<T>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Class<T> clazz;

    public JsonAttributeConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String convertToDatabaseColumn(List<T> attribute) {

        if (attribute == null || attribute.isEmpty()) {
            return "[]";
        }

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting object to JSON", e);
        }
    }

    @Override
    public List<T> convertToEntityAttribute(String dbData) {

        if (dbData == null || dbData.isBlank()) {
            return Collections.emptyList();
        }

        try {
            JavaType type = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, clazz);

            return objectMapper.readValue(dbData, type);

        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON to Object", e);
        }
    }
}