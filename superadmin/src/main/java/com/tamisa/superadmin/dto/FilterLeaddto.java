package com.tamisa.superadmin.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class FilterLeaddto {
    private String contactName1;

    private String partnerCode;
    private String status;
    private String Notes;
    private String email;
   private String phone;

   
   private String streetAddress;
  
   private String DistrictOrTown;
  
   private String state;
   
   private String pincode;
  
   private String country;
}
