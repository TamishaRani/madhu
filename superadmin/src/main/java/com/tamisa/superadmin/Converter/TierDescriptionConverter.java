package com.tamisa.superadmin.Converter;

import com.tamisa.superadmin.dto.TierDescriptionDTO;

import jakarta.persistence.Converter;

@Converter
public class TierDescriptionConverter extends JsonAttributeConverter<TierDescriptionDTO> {

    public TierDescriptionConverter() {
        super(TierDescriptionDTO.class);
    }

}