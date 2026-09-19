package com.tamisa.superadmin.dto;

import com.tamisa.superadmin.Enum.DocumentType;
import com.tamisa.superadmin.Enum.VerificationStatus;

import lombok.Data;

@Data
public class PartnerKYCDto {

    private Integer kycId;

    private Integer partnerId;

    private DocumentType documentType;

    private String documentNumber;

    private VerificationStatus verificationStatus;

    private Long verifiedBy;

    private String remarks;
}