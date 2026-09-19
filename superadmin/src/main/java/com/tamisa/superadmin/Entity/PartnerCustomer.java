package com.tamisa.superadmin.Entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "partner_customer")
@Data
public class PartnerCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId;

    private Integer partnerId;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "mobile", length = 15)
    private String mobile;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "pincode", length = 10)
    private String pincode;

    @Column(name = "status", length = 20)
    private String status = "ACTIVE";

    @Column(name = "approved_by")
    private int approvedBy;

    @Column(name = "approved_date")
    private LocalDateTime approvedDate;
    
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

}