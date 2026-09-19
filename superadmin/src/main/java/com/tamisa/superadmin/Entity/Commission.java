package com.tamisa.superadmin.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "tblpartnerCommission")
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commissionId;

    @Column(name = "commission_date")
    private LocalDate date;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "plan_name", length = 100)
    private String planName;

    @Column(name = "subscription_type", length = 50)
    private String subscriptionType;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "partner_id", length = 50)
    private String partnerId;

    @Column(name = "level", length = 50)
    private String level;

    @Column(name = "billing_cycle", length = 50)
    private String billingCycle;

    @Column(name = "commission_percent")
    private Double commissionPercent;

    @Column(name = "commission_amount")
    private Double commissionAmount;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "settlement_date")
    private LocalDate settlementDate;


    
    
}