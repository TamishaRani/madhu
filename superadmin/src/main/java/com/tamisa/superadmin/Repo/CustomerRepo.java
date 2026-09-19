package com.tamisa.superadmin.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.tamisa.superadmin.Entity.PartnerCustomer;

public interface CustomerRepo extends JpaRepository<PartnerCustomer, Integer> {


    List<PartnerCustomer> findByPartner_PartnerCode(String partnerCode);

}

