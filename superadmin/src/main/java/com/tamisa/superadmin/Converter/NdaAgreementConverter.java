package com.tamisa.superadmin.Converter;

import com.tamisa.superadmin.dto.NdaAgreementDTO;

import jakarta.persistence.Converter;

@Converter
public class NdaAgreementConverter
        extends JsonAttributeConverter<NdaAgreementDTO> {

    public NdaAgreementConverter() {
        super(NdaAgreementDTO.class);
    }
}