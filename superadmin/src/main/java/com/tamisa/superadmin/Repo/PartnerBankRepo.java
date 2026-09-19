package com.tamisa.superadmin.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tamisa.superadmin.Entity.PartnerBankDetails;


public interface PartnerBankRepo extends JpaRepository<PartnerBankDetails, Integer> {
 List<PartnerBankDetails> findByPartnerId(Integer partnerId);
 void deleteByPartnerId(Integer partnerId);

}
