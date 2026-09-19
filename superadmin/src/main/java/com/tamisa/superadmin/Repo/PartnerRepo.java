package com.tamisa.superadmin.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tamisa.superadmin.Entity.Partner;
import com.tamisa.superadmin.Entity.PartnerBankDetails;
import com.tamisa.superadmin.Entity.PartnerKYC;
import com.tamisa.superadmin.Entity.PartnerNda;

@Repository
public interface PartnerRepo
                extends JpaRepository<Partner, Integer>, JpaSpecificationExecutor<Partner> {
       Optional<Partner> findByPartnerCode(String partnerCode);
                }