package com.tamisa.superadmin.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tamisa.superadmin.Enum.OnboardingStatus;
import com.tamisa.superadmin.Enum.PartnerStatus;
import com.tamisa.superadmin.Enum.PartnerType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "tblpartner")
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partner_id")
    private Integer partnerId;
    @Column(name = "partner_code", length = 30)
    private String partnerCode;

    @Column(name = "company_name", length = 150)
    private String companyName;

    @Column(name = "legal_name", length = 150)
    private String legalName;

    @Column(name = "contact_person", length = 120)
    private String contactPerson;

    @Enumerated(EnumType.STRING)
    @Column(name = "partner_type")
    private PartnerType partnerType;

    @Column(name = "industry_type", length = 100)
    private String industryType;

    @Column(name = "interested_in_product", length = 45)
    private String interestedInProduct;


    // =========================
    // Tier / Partner Hierarchy
    // =========================

    @Column(name = "tier_id")
    private Long tierId;

    @Transient
    private String tierName;

    @Column(name = "parent_partner_id")
    private Long parentPartnerId;


    // =========================
    // Business Documents
    // =========================

    @Column(name = "gst", length = 15)
    private String gst;

    @Column(name = "pan", length = 10)
    private String pan;

    @Column(name = "cin", length = 25)
    private String cin;

    @Column(name = "msme_number", length = 25)
    private String msmeNumber;


    // =========================
    // Contact Information
    // =========================

    @Column(name = "email", length = 120)
    private String email;

    @Column(name = "mobile", length = 15)
    private String mobile;

    @Column(name = "alternate_mobile", length = 15)
    private String alternateMobile;

    @Column(name = "website", length = 200)
    private String website;


    // =========================
    // Address
    // =========================

    @Column(name = "country")
    private Integer country;

    @Column(name = "state")
    private Integer state;

    @Column(name = "city")
    private Integer city;

    @Column(name = "address_line", length = 250)
    private String addressLine;

    @Column(name = "pincode", length = 10)
    private String pincode;


    // =========================
    // Location
    // =========================

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;


    // =========================
    // Financial
    // =========================

    @Column(name = "credit_limit", precision = 18, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "wallet_balance", precision = 18, scale = 2)
    private BigDecimal walletBalance;


    // =========================
    // Management
    // =========================

    @Column(name = "sales_manager_id")
    private Long salesManagerId;


    // =========================
    // Status
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(name = "onboarding_status")
    private OnboardingStatus onboardingStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PartnerStatus status;


    // =========================
    // Approval
    // =========================

    @Column(name = "approved_by")
    private int approvedBy;

    @Column(name = "approved_date")
    private LocalDateTime approvedDate;


    // =========================
    // Audit
    // =========================

    @Column(name = "created_by")
    private int createdBy;

    @CreationTimestamp
    @Column(
        name = "created_date",
        nullable = false,
        updatable = false
    )
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    private int modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;


    // =========================
    // Rejection
    // =========================

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;


    // =========================
    // Bank Details
    // =========================

    @Transient
    @JsonManagedReference
    @OneToMany(
        mappedBy = "partner",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<PartnerBankDetails> bankDetails =
            new ArrayList<>();

}