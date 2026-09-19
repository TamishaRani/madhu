package com.tamisa.superadmin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.tamisa.superadmin.Entity.Partner;

import com.tamisa.superadmin.Entity.PartnerBankDetails;
import com.tamisa.superadmin.Entity.PartnerNda;
import com.tamisa.superadmin.Service.ItPartnerService;
import com.tamisa.superadmin.dto.Filterdto;
import com.tamisa.superadmin.dto.PartnerBankDetailsDto;
import com.tamisa.superadmin.dto.PartnerKYCDto;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tblPartners-Registration")
public class PartnerController {

    @Autowired
    private ItPartnerService ItPartnerService;

    @PostMapping(value = "/partner-create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> savePartner(

            @ModelAttribute Partner partner,

            @RequestParam(required = false) String bankDetailsJson,

            @RequestParam(required = false) String kycJson,

            @RequestParam(required = false) String ndaJson,

            @RequestParam(required = false) MultipartFile[] kycFiles,

            @RequestPart(value = "signatureFile", required = false) MultipartFile signatureFile,
             @RequestPart(value = "agreementFile", required = false) MultipartFile agreementFile,
         HttpServletRequest request) {

        Map<String, Object> res = new HashMap<>();

        if (partner.getPartnerId() == null || partner.getPartnerId() == 0) {

            Partner newPartner = ItPartnerService.create(
                    partner,
                        bankDetailsJson,
                        kycJson,
                        ndaJson,
                        kycFiles,
                        signatureFile,
                        agreementFile,
                        request
                );

            res.put("status", true);
            res.put("data", newPartner);
            res.put("message", "Partner Registered Successfully");

            return new ResponseEntity<>(res, HttpStatus.CREATED);

        } else {

            Partner updatedPartner = ItPartnerService.update(
                  partner,
                        partner.getPartnerId(),
                        bankDetailsJson,
                        kycJson,
                        ndaJson,
                        kycFiles,
                        signatureFile,
                        request);

            res.put("status", true);
            res.put("data", updatedPartner);
            res.put("message", "Partner Updated Successfully");

            return new ResponseEntity<>(res, HttpStatus.OK);
        }
    }

    // @PostMapping(value = "/bank-details",
    // consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // public ResponseEntity<?> saveBankDetails(

    // @ModelAttribute PartnerBankDetailsDto bankDetailsDto,

    // @RequestParam("cancelledCheque") MultipartFile cancelledCheque) {

    // Map<String, Object> res = new HashMap<>();

    // if (bankDetailsDto.getId() == null || bankDetailsDto.getId() == 0) {

    // PartnerBankDetails newBank =
    // PartnerService.saveBankDetails(
    // bankDetailsDto,
    // cancelledCheque);

    // res.put("status", true);
    // res.put("data", newBank);
    // res.put("message", "Bank Details Saved Successfully");

    // return new ResponseEntity<>(res, HttpStatus.CREATED);

    // } else {

    // PartnerBankDetails updatedBank =
    // PartnerService.updateBankDetails(
    // bankDetailsDto,
    // cancelledCheque);

    // res.put("status", true);
    // res.put("data", updatedBank);
    // res.put("message", "Bank Details Updated Successfully");

    // return new ResponseEntity<>(res, HttpStatus.OK);
    // }
    // }

    // @PostMapping(value = "/documents", consumes =
    // MediaType.MULTIPART_FORM_DATA_VALUE)
    // public ResponseEntity<?> saveDocuments(

    // @ModelAttribute PartnerDocumentDto documentDto,

    // @RequestParam("aadhaarFile") MultipartFile aadhaarFile,

    // @RequestParam("panFile") MultipartFile panFile,

    // @RequestParam("gstCertificate") MultipartFile gstCertificate,

    // @RequestParam("addressProof") MultipartFile addressProof,

    // @RequestParam("profilePhoto") MultipartFile profilePhoto,

    // @RequestParam(value = "companyRegistrationCertificate", required = false)
    // MultipartFile companyRegistrationCertificate) {

    // Map<String, Object> res = new HashMap<>();

    // if (documentDto.getId() == null || documentDto.getId() == 0) {

    // PartnerDocument newDocument = PartnerService.saveDocuments(
    // documentDto,
    // aadhaarFile,
    // panFile,
    // gstCertificate,
    // addressProof,
    // profilePhoto,
    // companyRegistrationCertificate);

    // res.put("status", true);
    // res.put("data", newDocument);
    // res.put("message", "Partner Documents Saved Successfully");

    // return new ResponseEntity<>(res, HttpStatus.CREATED);

    // } else {

    // PartnerDocument updatedDocument = PartnerService.updateDocuments(
    // documentDto,
    // aadhaarFile,
    // panFile,
    // gstCertificate,
    // addressProof,
    // profilePhoto,
    // companyRegistrationCertificate);

    // res.put("status", true);
    // res.put("data", updatedDocument);
    // res.put("message", "Partner Documents Updated Successfully");

    // return new ResponseEntity<>(res, HttpStatus.OK);
    // }
    // }

    // @PostMapping("/onboarding")
    // public ResponseEntity<?> onboarding(
    // @RequestBody Partner partner,
    // @RequestParam Long partnerId) {

    // Map<String, Object> res = new HashMap<>();

    // Partner onboardingPartner =
    // ItPartnerService.onboarding(partner, partnerId);

    // res.put("status", true);
    // res.put("data", onboardingPartner);
    // res.put("message", "Partner Updated Successfully");

    // return new ResponseEntity<>(res, HttpStatus.OK);
    // }

    @PostMapping("/getFilterPartner/{partnerId}")
    public ResponseEntity<?> getFilterPartner(

            @PathVariable Integer partnerId,

            @RequestBody(required = false) Filterdto dto,

            @RequestParam(defaultValue = "1") Integer page,

            @RequestParam(defaultValue = "10") Integer limit,

            @RequestParam(required = false) String searchText,

            @RequestParam(required = false) String date) {

        Page<Partner> result = ItPartnerService.getFilterPartner(
                partnerId,
                dto,
                page,
                limit,
                searchText,
                date);

        Map<String, Object> res = new HashMap<>();

        res.put("pageNo", result.getNumber() + 1);
        res.put("pageSize", result.getSize());
        res.put("totalRecords", result.getTotalElements());
        res.put("totalPages", result.getTotalPages());
        res.put("data", result.getContent());
        res.put("filters", dto);
        res.put("status", true);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/getPartnerById/{partnerId}")
    public ResponseEntity<?> getPartnerById(@PathVariable Integer partnerId) {

        Map<String, Object> res = new HashMap<>();

        Partner response = ItPartnerService.getPartnerById(partnerId);

        res.put("status", true);
        res.put("data", response);
        res.put("message", "Partner Fetched Successfully");

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/bank-details/{partnerId}")
    public ResponseEntity<?> getAllBankDetailsByPartnerId(
            @PathVariable Integer partnerId) {

        Map<String, Object> res = new HashMap<>();

        List<PartnerBankDetails> bankDetails = ItPartnerService.getAllBankDetailsByPartnerId(partnerId);

        res.put("status", true);
        res.put("data", bankDetails);
        res.put("message", "Bank Details Fetched Successfully");

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @GetMapping("/nda")
public ResponseEntity<?> getAllNda() {

    Map<String, Object> res = new HashMap<>();

    List<PartnerNda> ndaList = ItPartnerService.getAllNda();

    res.put("status", true);
    res.put("data", ndaList);
    res.put("message", "NDA Details Fetched Successfully");

    return new ResponseEntity<>(res, HttpStatus.OK);
}
@GetMapping("/partner-details/{partnerId}")
public ResponseEntity<?> getPartnerDetails(
        @PathVariable Integer partnerId) {

    Map<String, Object> res = new HashMap<>();

    Map<String, Object> data =
            ItPartnerService.getPartnerDetails(partnerId);

    res.put("status", true);
    res.put("data", data);
    res.put("message", "Partner Details Fetched Successfully");

    return new ResponseEntity<>(res, HttpStatus.OK);
}
}