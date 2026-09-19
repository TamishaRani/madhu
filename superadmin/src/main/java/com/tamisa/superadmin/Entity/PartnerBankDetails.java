package com.tamisa.superadmin.Entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tamisa.superadmin.Enum.AccountType;
import com.tamisa.superadmin.Enum.BankVerificationStatus;
import com.tamisa.superadmin.Enum.Status;
import com.tamisa.superadmin.Enum.VerificationStatus;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tblpartnerBankDetails")
@Data
public class PartnerBankDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_id")
    private Long bankId;
    //  @Transient
    //  @JsonBackReference
    //  @ManyToOne(fetch = FetchType.LAZY)
    //  @JoinColumn(name = "partner_id")
    // private Partner partner;

    private Integer partnerId;

    @Column(name = "account_holder_name", length = 150)
    private String accountHolderName;

    @Column(name = "bank_name", length = 150)
    private String bankName;

    @Column(name = "branch_name", length = 120)
    private String branchName;

    @Column(name = "account_number", length = 30)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    @Column(name = "ifsc_code", length = 11)
    private String ifscCode;

    @Column(name = "micr_code", length = 9)
    private String micrCode;

    @Column(name = "swift_code", length = 20)
    private String swiftCode;

    @Column(name = "upi_id", length = 100)
    private String upiId;

    @Column(name = "cancelled_cheque", length = 500)
    private String cancelledCheque;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    private VerificationStatus verificationStatus;

    @Column(name = "verified_by")
    private Long verifiedBy;

    @Column(name = "verified_date")
    private LocalDateTime verifiedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "created_by")
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

   
}