package com.tamisa.superadmin.Service;

import org.springframework.web.multipart.MultipartFile;

import com.tamisa.superadmin.Entity.Partner;
import com.tamisa.superadmin.Entity.PartnerBankDetails;
import com.tamisa.superadmin.Entity.PartnerNda;

import com.tamisa.superadmin.dto.Filterdto;
import com.tamisa.superadmin.dto.PartnerBankDetailsDto;

import com.tamisa.superadmin.dto.PartnerKYCDto;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

public interface ItPartnerService {

      Partner create(
        Partner partner,
        String bankDetailsJson,
        String kycJson,
        String ndaJson,
        MultipartFile[] kycFiles,
        MultipartFile signatureFile,
        MultipartFile agreementFile,
        HttpServletRequest request
);

Partner update(
        Partner partner,
        Integer partnerId,
        String bankDetailsJson,
        String kycJson,
        String ndaJson,
        MultipartFile[] kycFiles,
        MultipartFile signatureFile,
        HttpServletRequest request
);

        // PartnerBankDetails saveBankDetails(
        // PartnerBankDetailsDto bankDetailsDto,
        // MultipartFile cancelledCheque);

        // PartnerBankDetails updateBankDetails(
        // PartnerBankDetailsDto bankDetailsDto,
        // MultipartFile cancelledCheque);

        // PartnerDocument saveDocuments(
        // PartnerDocumentDto documentDto,
        // MultipartFile aadhaarFile,
        // MultipartFile panFile,
        // MultipartFile gstCertificate,
        // MultipartFile addressProof,
        // MultipartFile profilePhoto,
        // MultipartFile companyRegistrationCertificate);

        // PartnerDocument updateDocuments(
        // PartnerDocumentDto documentDto,
        // MultipartFile aadhaarFile,
        // MultipartFile panFile,
        // MultipartFile gstCertificate,
        // MultipartFile addressProof,
        // MultipartFile profilePhoto,
        // MultipartFile companyRegistrationCertificate);

        Partner getPartnerById(Integer partnerId);

        // Partner onboarding(Partner partner, Long id);

        Page<Partner> getFilterPartner(
                        Integer id,
                        Filterdto dto,
                        Integer page,
                        Integer limit,
                        String searchText,
                        String date);

        List<PartnerBankDetails> getAllBankDetailsByPartnerId(Integer partnerId);
         List<PartnerNda> getAllNda();
         Map<String, Object> getPartnerDetails(Integer partnerId);

}