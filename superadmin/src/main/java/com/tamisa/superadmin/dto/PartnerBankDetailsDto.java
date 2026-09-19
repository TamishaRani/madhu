package com.tamisa.superadmin.dto;

import lombok.Data;

@Data
public class PartnerBankDetailsDto {

    private Integer id;

    private Integer partnerId;

    private String accountHolderName;

    private String accountNumber;

    private String ifscCode;

    private String bankName;

    private Integer createdBy;

    private String createdUser;

    private Integer updatedBy;
}