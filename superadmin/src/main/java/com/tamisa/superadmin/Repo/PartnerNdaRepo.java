package com.tamisa.superadmin.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tamisa.superadmin.Entity.Partner;
import com.tamisa.superadmin.Entity.PartnerNda;

@Repository
public interface PartnerNdaRepo extends JpaRepository<PartnerNda,Integer> {

    List<PartnerNda> findByPartnerId(Integer partnerId);

   

}