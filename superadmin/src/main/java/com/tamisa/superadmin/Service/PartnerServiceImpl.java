package com.tamisa.superadmin.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.tamisa.superadmin.Entity.Partner;
import com.tamisa.superadmin.Entity.PartnerBankDetails;
import com.tamisa.superadmin.Entity.PartnerKYC;
import com.tamisa.superadmin.Entity.PartnerNda;

import com.tamisa.superadmin.Enum.AccountType;
import com.tamisa.superadmin.Enum.DocumentType;
import com.tamisa.superadmin.Enum.NDAStatus;
import com.tamisa.superadmin.Enum.OnboardingStatus;
import com.tamisa.superadmin.Enum.PartnerStatus;
import com.tamisa.superadmin.Enum.SignatureMethod;
import com.tamisa.superadmin.Enum.VerificationStatus;
import com.tamisa.superadmin.Repo.PartnerBankRepo;
import com.tamisa.superadmin.Repo.PartnerKYCRepo;
import com.tamisa.superadmin.Repo.PartnerNdaRepo;
import com.tamisa.superadmin.Repo.PartnerRepo;

import com.tamisa.superadmin.Repo.TierRepo;

import com.tamisa.superadmin.dto.Filterdto;
import com.tamisa.superadmin.dto.NdaAgreementDTO;
import com.tamisa.superadmin.dto.PartnerBankDetailsDto;

import com.tamisa.superadmin.dto.PartnerKYCDto;

import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PartnerServiceImpl implements ItPartnerService {
        @Autowired
        private PartnerRepo PartnerRepo;
        @Autowired
        private PartnerBankRepo bankRepo;
        @Autowired
        private PartnerKYCRepo partnerKYCRepo;

        @Autowired
        private PartnerNdaRepo partnerNdaRepo;
        @Value("${file.upload.path}")
        private String uploadPath;

        @Override
        @Transactional
        public Partner create(
                        Partner partner,
                        String bankDetailsJson,
                        String kycJson,
                        String ndaJson,
                        MultipartFile[] kycFiles,
                        MultipartFile signatureFile,
                         MultipartFile agreementFile,
                        HttpServletRequest request) {

                try {

                        System.out.println("Bank JSON = " + bankDetailsJson);
                        System.out.println("KYC JSON = " + kycJson);
                        System.out.println("NDA JSON = " + ndaJson);

                        partner.setTierId(1L);
                        partner.setStatus(PartnerStatus.ACTIVE);
                        partner.setOnboardingStatus(OnboardingStatus.Registration);

                        log.info(
                                        "Onboarding Status: {}",
                                        partner.getOnboardingStatus());

                        Partner savedPartner = PartnerRepo.save(partner);

                        Integer partnerId = savedPartner.getPartnerId();

                        if (bankDetailsJson != null
                                        && !bankDetailsJson.isBlank()) {

                                JSONArray bankArray = new JSONArray(bankDetailsJson);

                                List<PartnerBankDetails> bankList = new ArrayList<>();

                                for (int i = 0; i < bankArray.length(); i++) {

                                        JSONObject obj = bankArray.getJSONObject(i);

                                        PartnerBankDetails bank = new PartnerBankDetails();

                                        bank.setPartnerId(partnerId);

                                        bank.setAccountHolderName(
                                                        obj.optString("accountHolderName"));

                                        bank.setBankName(
                                                        obj.optString("bankName"));

                                        bank.setBranchName(
                                                        obj.optString("branchName"));

                                        bank.setAccountNumber(
                                                        obj.optString("accountNumber"));

                                        if (!obj.optString("accountType")
                                                        .isEmpty()) {

                                                bank.setAccountType(
                                                                AccountType.valueOf(
                                                                                obj.optString(
                                                                                                "accountType")
                                                                                                .toUpperCase()));
                                        }

                                        bank.setIfscCode(
                                                        obj.optString("ifscCode"));

                                        bank.setMicrCode(
                                                        obj.optString("micrCode"));

                                        bank.setSwiftCode(
                                                        obj.optString("swiftCode"));

                                        bank.setUpiId(
                                                        obj.optString("upiId"));

                                        bank.setCancelledCheque(
                                                        obj.optString("cancelledCheque"));

                                        bank.setIsPrimary(
                                                        obj.optBoolean(
                                                                        "isPrimary",
                                                                        false));
                                        bank.setVerificationStatus(
                                                        VerificationStatus.PENDING);

        
bankList.add(bank);
                                }

                                bankRepo.saveAll(bankList);

                                savedPartner.setOnboardingStatus(
                                                OnboardingStatus.BankDetails_APPROVED);

                                PartnerRepo.save(savedPartner);

                                System.out.println("Bank saved");
                        }

                        if (kycJson != null && !kycJson.isBlank()) {

                                File folder = new File(uploadPath);

                                if (!folder.exists()) {
                                        folder.mkdirs();
                                }

                                JSONObject obj = new JSONObject(kycJson);

                                PartnerKYC kyc = new PartnerKYC();

                                kyc.setPartnerId(partnerId);

                                String documentType = obj.optString("documentType");

                                if (!documentType.isBlank()) {

                                        kyc.setDocumentType(
                                                        DocumentType.valueOf(
                                                                        documentType.toUpperCase()));
                                }

                                kyc.setDocumentNumber(
                                                obj.optString("documentNumber"));

                                if (kycFiles != null
                                                && kycFiles.length > 0) {

                                        MultipartFile file = kycFiles[0];

                                        if (file != null
                                                        && !file.isEmpty()) {

                                                String originalFileName = file.getOriginalFilename();

                                                String fileName = System.currentTimeMillis()
                                                                + "_"
                                                                + originalFileName;

                                                Path path = Paths.get(
                                                                uploadPath,
                                                                fileName);

                                                Files.copy(
                                                                file.getInputStream(),
                                                                path,
                                                                StandardCopyOption.REPLACE_EXISTING);

                                                kyc.setFileName(fileName);

                                                kyc.setFilePath(
                                                                path.toString());

                                                kyc.setFileSize(
                                                                file.getSize());
                                        }
                                }

                                kyc.setVerificationStatus(
                                                VerificationStatus.PENDING);
                                kyc.setAccepted(true);

                                if (!obj.isNull("verifiedBy")) {

                                        kyc.setVerifiedBy(
                                                        obj.getLong("verifiedBy"));
                                }

                                if (obj.has("remarks")) {

                                        kyc.setRemarks(
                                                        obj.optString("remarks"));
                                }

                                partnerKYCRepo.save(kyc);

                                System.out.println(
                                                "KYC saved successfully");
                        }

                        if (ndaJson != null
                                        && !ndaJson.isBlank()) {

                                JSONObject obj = new JSONObject(ndaJson);

                                PartnerNda nda = new PartnerNda();

                                nda.setPartnerId(partnerId);

                                nda.setNdaVersion(
                                                obj.optString("ndaVersion"));
                                                if (agreementFile != null && !agreementFile.isEmpty()) {

    File folder = new File(uploadPath);

    if (!folder.exists()) {
        folder.mkdirs();
    }

    String originalFileName = agreementFile.getOriginalFilename();

    String fileName = System.currentTimeMillis()
            + "_"
            + originalFileName;

    Path path = Paths.get(
            uploadPath,
            fileName);

    Files.copy(
            agreementFile.getInputStream(),
            path,
            StandardCopyOption.REPLACE_EXISTING);

    nda.setAgreementFile(fileName);
   

    System.out.println(
            "Agreement file uploaded successfully: " + path);
}

                                ObjectMapper mapper = new ObjectMapper();

                                List<NdaAgreementDTO> agreementList = mapper.readValue(
                                                obj.getJSONArray(
                                                                "agreementTitle")
                                                                .toString(),
                                                new TypeReference<List<NdaAgreementDTO>>() {
                                                });

                                nda.setAgreementTitle(
                                                agreementList);

                                String signatureMethodValue = obj.optString("signatureMethod");

                                if (!signatureMethodValue.isBlank()) {

                                        SignatureMethod signatureMethod;

                                        try {

                                                signatureMethod = SignatureMethod.valueOf(
                                                                signatureMethodValue
                                                                                .toUpperCase());

                                        } catch (IllegalArgumentException e) {

                                                throw new RuntimeException(
                                                                "Invalid signature method: "
                                                                                + signatureMethodValue);
                                        }

                                        nda.setSignatureMethod(
                                                        signatureMethod);

                                        if (signatureMethod == SignatureMethod.DRAW_SIGNATURE
                                                        || signatureMethod == SignatureMethod.MANUAL_SIGNATURE) {

                                                if (signatureFile == null
                                                                || signatureFile.isEmpty()) {

                                                        throw new RuntimeException(
                                                                        "Signature file is required for "
                                                                                        + signatureMethod);
                                                }

                                                File folder = new File(uploadPath);

                                                if (!folder.exists()) {
                                                        folder.mkdirs();
                                                }

                                                String originalFileName = signatureFile.getOriginalFilename();

                                                String fileName = System.currentTimeMillis()
                                                                + "_"
                                                                + originalFileName;

                                                Path path = Paths.get(
                                                                uploadPath,
                                                                fileName);

                                                Files.copy(
                                                                signatureFile.getInputStream(),
                                                                path,
                                                                StandardCopyOption.REPLACE_EXISTING);

                                                
                                                nda.setSignedPdf(path.toString());

                                                System.out.println(
                                                                "Signed PDF uploaded successfully: "
                                                                                + path);
                                        }
                                }

                                nda.setSignerName(
                                                obj.optString("signerName"));

                                nda.setSignerDesignation(
                                                obj.optString(
                                                                "signerDesignation"));

                                String ipAddress = request.getHeader(
                                                "X-Forwarded-For");

                                if (ipAddress == null
                                                || ipAddress.isBlank()
                                                || "unknown".equalsIgnoreCase(
                                                                ipAddress)) {

                                        ipAddress = request.getHeader(
                                                        "Proxy-Client-IP");
                                }

                                if (ipAddress == null
                                                || ipAddress.isBlank()
                                                || "unknown".equalsIgnoreCase(
                                                                ipAddress)) {

                                        ipAddress = request.getHeader(
                                                        "WL-Proxy-Client-IP");
                                }

                                if (ipAddress == null
                                                || ipAddress.isBlank()
                                                || "unknown".equalsIgnoreCase(
                                                                ipAddress)) {

                                        ipAddress = request.getRemoteAddr();
                                }

                                if (ipAddress != null
                                                && ipAddress.contains(",")) {

                                        ipAddress = ipAddress
                                                        .split(",")[0]
                                                        .trim();
                                }

                                nda.setSignIPAddress(ipAddress);

                                String userAgent = request.getHeader("User-Agent");

                                String deviceInfo = "Unknown";

                                if (userAgent != null
                                                && !userAgent.isBlank()) {

                                        if (userAgent.matches(
                                                        ".*(iPhone|Android|Mobile).*")) {

                                                deviceInfo = "Mobile";

                                        } else if (userAgent.matches(
                                                        ".*(iPad|Tablet).*")) {

                                                deviceInfo = "Tablet";

                                        } else {

                                                deviceInfo = "Desktop";
                                        }
                                }

                                nda.setDeviceInfo(deviceInfo);

                                String browserInfo = "Unknown";

                                if (userAgent != null
                                                && !userAgent.isBlank()) {

                                        if (userAgent.contains("Edg/")) {

                                                browserInfo = "Microsoft Edge";

                                        } else if (userAgent.contains("OPR/")
                                                        || userAgent.contains("Opera/")) {

                                                browserInfo = "Opera";

                                        } else if (userAgent.contains("Chrome/")
                                                        && !userAgent.contains("Edg/")) {

                                                browserInfo = "Google Chrome";

                                        } else if (userAgent.contains("Firefox/")) {

                                                browserInfo = "Mozilla Firefox";

                                        } else if (userAgent.contains("Safari/")
                                                        && !userAgent.contains("Chrome/")) {

                                                browserInfo = "Safari";
                                        }
                                }

                                nda.setBrowserInfo(browserInfo);

                                nda.setSignedDate(
                                                LocalDateTime.now());

                                if (!obj.optString(
                                                "expiryDate").isBlank()) {

                                        nda.setExpiryDate(
                                                        LocalDate.parse(
                                                                        obj.optString(
                                                                                        "expiryDate")));
                                }

                                if (!obj.optString("status")
                                                .isBlank()) {

                                        nda.setStatus(
                                                        NDAStatus.valueOf(
                                                                        obj.optString("status")
                                                                                        .toUpperCase()));
                                }

                                partnerNdaRepo.save(nda);

                                savedPartner.setOnboardingStatus(
                                                OnboardingStatus.ONBOARDED);

                                PartnerRepo.save(savedPartner);

                                System.out.println(
                                                "NDA Saved Successfully");
                        }

                        return savedPartner;

                } catch (Exception e) {

                        e.printStackTrace();

                        throw new RuntimeException(
                                        "Partner Registration Failed : "
                                                        + e.getMessage());
                }
        }

        @Override
        public Partner getPartnerById(Integer partnerId) {
                Partner get = PartnerRepo.findById(partnerId).orElseThrow(() -> new RuntimeException(
                                "Partner not found"));
                return get;
        }

        @Override
        public List<PartnerBankDetails> getAllBankDetailsByPartnerId(Integer partnerId) {

                PartnerRepo.findById(partnerId)
                                .orElseThrow(() -> new RuntimeException("Partner Not Found"));

                return bankRepo.findByPartnerId(partnerId);
        }

        @Override
        public Partner update(Partner partner, Integer partnerId, String bankDetailsJson, String kycJson,
                        String ndaJson,
                        MultipartFile[] kycFiles, MultipartFile signatureFile, HttpServletRequest request) {
                try {

                        Partner existingPartner = PartnerRepo.findById(partnerId)
                                        .orElseThrow(() -> new RuntimeException("Partner Not Found"));

                        existingPartner.setCompanyName(
                                        partner.getCompanyName());

                        existingPartner.setLegalName(
                                        partner.getLegalName());

                        existingPartner.setContactPerson(
                                        partner.getContactPerson());

                        existingPartner.setPartnerType(
                                        partner.getPartnerType());

                        existingPartner.setIndustryType(
                                        partner.getIndustryType());

                        existingPartner.setInterestedInProduct(
                                        partner.getInterestedInProduct());

                        existingPartner.setEmail(
                                        partner.getEmail());

                        existingPartner.setMobile(
                                        partner.getMobile());

                        existingPartner.setAlternateMobile(
                                        partner.getAlternateMobile());

                        existingPartner.setWebsite(
                                        partner.getWebsite());

                        existingPartner.setCountry(
                                        partner.getCountry());

                        existingPartner.setState(
                                        partner.getState());

                        existingPartner.setCity(
                                        partner.getCity());

                        existingPartner.setAddressLine(
                                        partner.getAddressLine());

                        existingPartner.setPincode(
                                        partner.getPincode());

                        existingPartner.setModifiedBy(
                                        partner.getModifiedBy());

                        Partner savedPartner = PartnerRepo.save(existingPartner);

                        if (bankDetailsJson != null
                                        && !bankDetailsJson.isBlank()) {

                                bankRepo.deleteByPartnerId(
                                                savedPartner.getPartnerId());

                                JSONArray bankArray = new JSONArray(bankDetailsJson);

                                List<PartnerBankDetails> bankList = new ArrayList<>();

                                for (int i = 0; i < bankArray.length(); i++) {

                                        JSONObject obj = bankArray.getJSONObject(i);

                                        PartnerBankDetails bank = new PartnerBankDetails();

                                        bank.setPartnerId(
                                                        savedPartner.getPartnerId());

                                        bank.setAccountHolderName(
                                                        obj.optString(
                                                                        "accountHolderName"));

                                        bank.setBankName(
                                                        obj.optString("bankName"));

                                        bank.setBranchName(
                                                        obj.optString("branchName"));

                                        bank.setAccountNumber(
                                                        obj.optString("accountNumber"));

                                        if (!obj.optString(
                                                        "accountType").isEmpty()) {

                                                bank.setAccountType(
                                                                AccountType.valueOf(
                                                                                obj.optString(
                                                                                                "accountType")));
                                        }

                                        bank.setIfscCode(
                                                        obj.optString("ifscCode"));

                                        bank.setMicrCode(
                                                        obj.optString("micrCode"));

                                        bank.setSwiftCode(
                                                        obj.optString("swiftCode"));

                                        bank.setUpiId(
                                                        obj.optString("upiId"));

                                        bank.setCancelledCheque(
                                                        obj.optString(
                                                                        "cancelledCheque"));

                                        bank.setIsPrimary(
                                                        obj.optBoolean(
                                                                        "isPrimary",
                                                                        false));

                                        bankList.add(bank);
                                }

                                bankRepo.saveAll(bankList);
                        }

                        if (kycJson != null && !kycJson.isBlank()) {

                                partnerKYCRepo.deleteByPartnerId(
                                                savedPartner.getPartnerId());

                                File folder = new File(uploadPath);

                                if (!folder.exists()) {
                                        folder.mkdirs();
                                }

                                JSONObject obj = new JSONObject(kycJson);

                                PartnerKYC kyc = new PartnerKYC();

                                kyc.setPartnerId(
                                                savedPartner.getPartnerId());

                                String documentType = obj.optString("documentType");

                                if (!documentType.isBlank()) {

                                        kyc.setDocumentType(
                                                        DocumentType.valueOf(
                                                                        documentType.toUpperCase()));
                                }

                                kyc.setDocumentNumber(
                                                obj.optString("documentNumber"));

                                if (kycFiles != null
                                                && kycFiles.length > 0) {

                                        MultipartFile file = kycFiles[0];

                                        if (file != null && !file.isEmpty()) {

                                                String fileName = System.currentTimeMillis()
                                                                + "_"
                                                                + file.getOriginalFilename();

                                                Path path = Paths.get(
                                                                uploadPath,
                                                                fileName);

                                                Files.copy(
                                                                file.getInputStream(),
                                                                path,
                                                                StandardCopyOption.REPLACE_EXISTING);

                                                kyc.setFileName(fileName);
                                                kyc.setFilePath(path.toString());
                                                kyc.setFileSize(file.getSize());
                                        }
                                }

                                kyc.setVerificationStatus(
                                                VerificationStatus.PENDING);
                                kyc.setAccepted(true);

                                if (!obj.isNull("verifiedBy")) {
                                        kyc.setVerifiedBy(
                                                        obj.getLong("verifiedBy"));
                                }

                                if (obj.has("remarks")) {
                                        kyc.setRemarks(
                                                        obj.optString("remarks"));
                                }

                                partnerKYCRepo.save(kyc);

                                System.out.println(
                                                "KYC updated successfully");
                        }
                        if (ndaJson != null && !ndaJson.isBlank()) {

                                JSONObject obj = new JSONObject(ndaJson);

                                List<PartnerNda> ndaList = partnerNdaRepo.findByPartnerId(
                                                savedPartner.getPartnerId());
                                PartnerNda nda = ndaList.stream()
                                                .findFirst()
                                                .orElseThrow(() -> new RuntimeException("Partner NDA not found"));

                                nda.setPartnerId(savedPartner.getPartnerId());

                                nda.setNdaVersion(
                                                obj.optString("ndaVersion"));

                                ObjectMapper mapper = new ObjectMapper();

                                List<NdaAgreementDTO> agreementList = mapper.readValue(
                                                obj.getJSONArray("agreementTitle").toString(),
                                                new TypeReference<List<NdaAgreementDTO>>() {
                                                });

                                nda.setAgreementTitle(agreementList);

                                String signatureMethodValue = obj.optString("signatureMethod");

                                if (!signatureMethodValue.isBlank()) {

                                        SignatureMethod signatureMethod;

                                        try {

                                                signatureMethod = SignatureMethod.valueOf(
                                                                signatureMethodValue.toUpperCase());

                                        } catch (IllegalArgumentException e) {

                                                throw new RuntimeException(
                                                                "Invalid signature method: "
                                                                                + signatureMethodValue);
                                        }

                                        nda.setSignatureMethod(signatureMethod);

                                        if (signatureMethod == SignatureMethod.DRAW_SIGNATURE
                                                        || signatureMethod == SignatureMethod.MANUAL_SIGNATURE) {

                                                if (signatureFile != null && !signatureFile.isEmpty()) {

                                                        File folder = new File(uploadPath);

                                                        if (!folder.exists()) {
                                                                folder.mkdirs();
                                                        }

                                                        String originalFileName = signatureFile.getOriginalFilename();

                                                        String fileName = System.currentTimeMillis()
                                                                        + "_"
                                                                        + originalFileName;

                                                        Path path = Paths.get(
                                                                        uploadPath,
                                                                        fileName);

                                                        Files.copy(
                                                                        signatureFile.getInputStream(),
                                                                        path,
                                                                        StandardCopyOption.REPLACE_EXISTING);

                                                        System.out.println(
                                                                        "Signature updated successfully: "
                                                                                        + path);
                                                }
                                        }
                                }

                                nda.setSignerName(
                                                obj.optString("signerName"));

                                nda.setSignerDesignation(
                                                obj.optString("signerDesignation"));

                                // IP Address
                                String ipAddress = request.getHeader("X-Forwarded-For");

                                if (ipAddress == null
                                                || ipAddress.isBlank()
                                                || "unknown".equalsIgnoreCase(ipAddress)) {

                                        ipAddress = request.getHeader(
                                                        "Proxy-Client-IP");
                                }

                                if (ipAddress == null
                                                || ipAddress.isBlank()
                                                || "unknown".equalsIgnoreCase(ipAddress)) {

                                        ipAddress = request.getHeader(
                                                        "WL-Proxy-Client-IP");
                                }

                                if (ipAddress == null
                                                || ipAddress.isBlank()
                                                || "unknown".equalsIgnoreCase(ipAddress)) {

                                        ipAddress = request.getRemoteAddr();
                                }

                                if (ipAddress != null
                                                && ipAddress.contains(",")) {

                                        ipAddress = ipAddress
                                                        .split(",")[0]
                                                        .trim();
                                }

                                nda.setSignIPAddress(ipAddress);

                                // User Agent
                                String userAgent = request.getHeader("User-Agent");

                                String deviceInfo = "Unknown";

                                if (userAgent != null && !userAgent.isBlank()) {

                                        if (userAgent.matches(
                                                        ".*(iPhone|Android|Mobile).*")) {

                                                deviceInfo = "Mobile";

                                        } else if (userAgent.matches(
                                                        ".*(iPad|Tablet).*")) {

                                                deviceInfo = "Tablet";

                                        } else {

                                                deviceInfo = "Desktop";
                                        }
                                }

                                nda.setDeviceInfo(deviceInfo);

                                // Browser
                                String browserInfo = "Unknown";

                                if (userAgent != null && !userAgent.isBlank()) {

                                        if (userAgent.contains("Edg/")) {

                                                browserInfo = "Microsoft Edge";

                                        } else if (userAgent.contains("OPR/")
                                                        || userAgent.contains("Opera/")) {

                                                browserInfo = "Opera";

                                        } else if (userAgent.contains("Chrome/")
                                                        && !userAgent.contains("Edg/")) {

                                                browserInfo = "Google Chrome";

                                        } else if (userAgent.contains("Firefox/")) {

                                                browserInfo = "Mozilla Firefox";

                                        } else if (userAgent.contains("Safari/")
                                                        && !userAgent.contains("Chrome/")) {

                                                browserInfo = "Safari";
                                        }
                                }

                                nda.setBrowserInfo(browserInfo);

                                nda.setSignedDate(LocalDateTime.now());

                                // Expiry Date
                                if (!obj.optString("expiryDate").isBlank()) {

                                        nda.setExpiryDate(
                                                        LocalDate.parse(
                                                                        obj.optString("expiryDate")));
                                }

                                // Status
                                if (!obj.optString("status").isBlank()) {

                                        nda.setStatus(
                                                        NDAStatus.valueOf(
                                                                        obj.optString("status")
                                                                                        .toUpperCase()));
                                }

                                partnerNdaRepo.save(nda);

                                savedPartner.setOnboardingStatus(
                                                OnboardingStatus.ONBOARDED);

                                PartnerRepo.save(savedPartner);

                                System.out.println(
                                                "NDA Updated Successfully");
                        }

                        return savedPartner;

                } catch (Exception e) {

                        e.printStackTrace();

                        throw new RuntimeException(
                                        "Partner Update Failed : "
                                                        + e.getMessage());
                }

        }

        @Override
        public Page<Partner> getFilterPartner(Integer id, Filterdto dto, Integer page, Integer limit, String searchText,
                        String date) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                Pageable pageRequest = PageRequest.of(
                                Math.max(page - 1, 0),
                                limit,
                                Sort.by(Sort.Direction.DESC, "partnerId"));

                Specification<Partner> specification = (root, query, cb) -> cb.conjunction();

                if (searchText != null && !searchText.trim().isEmpty()) {

                        Specification<Partner> globalFilters = (root, query, cb) -> {

                                List<Predicate> predicateList = new ArrayList<>();

                                predicateList.add(cb.like(
                                                cb.lower(root.get("email")),
                                                "%" + searchText.toLowerCase() + "%"));

                                predicateList.add(cb.like(
                                                cb.lower(root.get("contactPerson")),
                                                "%" + searchText.toLowerCase() + "%"));

                                predicateList.add(cb.like(
                                                cb.lower(root.get("companyName")),
                                                "%" + searchText.toLowerCase() + "%"));

                                return cb.or(predicateList.toArray(new Predicate[0]));
                        };

                        specification = specification.and(globalFilters);
                }

                if (date != null && !date.trim().isEmpty()) {

                        String[] dateRange = date.split("to");

                        if (dateRange.length == 2) {

                                LocalDate startDate = LocalDate.parse(dateRange[0].trim(), formatter);
                                LocalDate endDate = LocalDate.parse(dateRange[1].trim(), formatter);

                                Specification<Partner> dateSpec = (root, query, cb) -> cb.between(
                                                root.get("createdDate"),
                                                startDate.atStartOfDay(),
                                                endDate.atTime(23, 59, 59));

                                specification = specification.and(dateSpec);
                        }
                }

                if (dto != null) {

                        Specification<Partner> additionalFilters = (root, query, cb) -> {

                                List<Predicate> predicateList = new ArrayList<>();

                                if (dto.getEmail() != null &&
                                                !dto.getEmail().trim().isEmpty() &&
                                                !dto.getEmail().equalsIgnoreCase("string")) {

                                        predicateList.add(cb.like(
                                                        cb.lower(root.get("email")),
                                                        "%" + dto.getEmail().toLowerCase() + "%"));
                                }

                                if (dto.getContactPerson() != null &&
                                                !dto.getContactPerson().trim().isEmpty() &&
                                                !dto.getContactPerson().equalsIgnoreCase("string")) {

                                        predicateList.add(cb.like(
                                                        cb.lower(root.get("contactPerson")),
                                                        "%" + dto.getContactPerson().toLowerCase() + "%"));
                                }

                                if (predicateList.isEmpty()) {
                                        return cb.conjunction();
                                }

                                return cb.and(predicateList.toArray(new Predicate[0]));
                        };

                        specification = specification.and(additionalFilters);
                }

                return PartnerRepo.findAll(specification, pageRequest);
        }
        @Override
        public List<PartnerNda> getAllNda() {

    return partnerNdaRepo.findAll();
}
@Override
public Map<String, Object> getPartnerDetails(Integer partnerId) {

    Map<String, Object> data = new HashMap<>();

   
    Partner partner = PartnerRepo.findById(partnerId)
            .orElseThrow(() ->
                    new RuntimeException("Partner not found"));


   

    PartnerKYC kyc =
            partnerKYCRepo.findByPartnerId(partnerId);


  

    List<PartnerBankDetails> bankDetails =
            bankRepo.findByPartnerId(partnerId);


   
    List<PartnerNda> nda =
            partnerNdaRepo.findByPartnerId(partnerId);


   
    data.put("partner", partner);
    data.put("partnerKyc", kyc);
    data.put("partnerBankDetails", bankDetails);
    data.put("partnerNda", nda);


    return data;
}

}