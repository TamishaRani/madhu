package com.tamisa.superadmin.Entity;

import java.io.File;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.tamisa.superadmin.Enum.DocumentType;
import com.tamisa.superadmin.Enum.VerificationStatus;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tblpartnerKYC")
@Data
public class PartnerKYC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kyc_id")
    private Integer kycId;

    @Column(name = "partner_id", nullable = false)
    private Integer partnerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType documentType;

    @Column(name = "document_number", length = 50)
    private String documentNumber;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "accepted", nullable = false)
    private Boolean accepted;
    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    private VerificationStatus verificationStatus;


  
    @Column(name = "verified_by")
    private Long verifiedBy;

    @Column(name = "verified_date")
    private LocalDateTime verifiedDate;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
}