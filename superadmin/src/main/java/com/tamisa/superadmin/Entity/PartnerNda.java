package com.tamisa.superadmin.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import com.tamisa.superadmin.Converter.NdaAgreementConverter;
import com.tamisa.superadmin.Enum.NDAStatus;
import com.tamisa.superadmin.Enum.SignatureMethod;
import com.tamisa.superadmin.dto.NdaAgreementDTO;

@Entity
@Data
@Table(name = "tblpartner_Digital_NDA")
public class PartnerNda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nda_id")
    private Integer ndaId;

    @Column(name = "partner_id", nullable = false)
    private Integer partnerId;

    @Column(name = "nda_version", length = 20)
    private String ndaVersion;

    @Convert(converter = NdaAgreementConverter.class)
    @Column(name = "agreement_title", columnDefinition = "LONGTEXT")
    private List<NdaAgreementDTO> agreementTitle = new ArrayList<>();

    @Column(name = "agreement_file", length = 500)
    private String agreementFile;

    @Column(name = "signed_pdf", length = 500)
    private String signedPdf;

    @Enumerated(EnumType.STRING)
    @Column(name = "signature_method")
    private SignatureMethod signatureMethod;

    @Column(name = "signer_name", length = 120)
    private String signerName;

    @Column(name = "signer_designation", length = 120)
    private String signerDesignation;

    @Column(name = "sign_ip_address", length = 45)
    private String signIPAddress;

    @Column(name = "device_info", length = 255)
    private String deviceInfo;

    @Column(name = "browser_info", length = 255)
    private String browserInfo;

    @Column(name = "signed_date")
    private LocalDateTime signedDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NDAStatus status;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
}