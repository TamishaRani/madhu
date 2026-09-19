 package com.tamisa.superadmin.Service;

 import java.util.List;

import org.springframework.data.domain.Page;
 import org.springframework.web.bind.annotation.RequestParam;
 import org.springframework.web.multipart.MultipartFile;

import com.tamisa.superadmin.Entity.Commission;
import com.tamisa.superadmin.Entity.Partner;
import com.tamisa.superadmin.Entity.PartnerBankDetails;
import com.tamisa.superadmin.Entity.PartnerCustomer;


 public interface ItadminService {
 

  public Partner approveOrRejectPartner(int id, String status, String tierName,String RejectionReason);

  
  public void deleteById(int id);

  public void deleteAll();

 
  Partner reviewApplication(Integer id);


  List<Partner> getAllPartners();


  Partner getPartnerById(int id);

  PartnerCustomer createCustomer(
            String partnerCode,
            PartnerCustomer customer
    );

List<PartnerCustomer> getCustomersByPartnerCode(String partnerCode);

Commission createCommission(Commission commission);

Commission updateCommission(Integer id, Commission commission);

Commission getCommissionById(Integer id);

List<Commission> getCommissionByPartnerId(Integer partnerId);

void deleteCommission(Integer id);

 }




