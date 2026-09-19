package com.tamisa.superadmin.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.tamisa.superadmin.Converter.TierDescriptionConverter;
import com.tamisa.superadmin.Enum.TierStatus;

import com.tamisa.superadmin.dto.TierDescriptionDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tblpartnertier")
@Data
public class Tier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tier_id")
    private Long tierId;

    @Column(name = "tier_code", length = 20, nullable = false, unique = true)
    private String tierCode;

    @Column(name = "tier_name", length = 50, nullable = false)
    private String tierName;

    @Convert(converter = TierDescriptionConverter.class)
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private List<TierDescriptionDTO> description = new ArrayList<>();

    @Column(name = "minimum_sales", precision = 18, scale = 2, nullable = false)
    private BigDecimal minimumSales;

    @Column(name = "minimum_renewal", nullable = false)
    private Integer minimumRenewal;

    @Column(name = "sale_commission", precision = 5, scale = 2, nullable = false)
    private BigDecimal saleCommission;

    @Column(name = "renewal_commission", precision = 5, scale = 2, nullable = false)
    private BigDecimal renewalCommission;

    @Column(name = "upgrade_commission", precision = 5, scale = 2, nullable = false)
    private BigDecimal upgradeCommission;

    @Column(name = "referral_commission", precision = 5, scale = 2)
    private BigDecimal referralCommission;

    @Column(name = "priority_support", nullable = false)
    private Boolean prioritySupport;

    @Column(name = "dedicated_manager", nullable = false)
    private Boolean dedicatedManager;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TierStatus status;

    @Column(name = "created_by")
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

}