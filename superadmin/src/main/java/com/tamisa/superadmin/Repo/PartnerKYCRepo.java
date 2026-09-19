package com.tamisa.superadmin.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tamisa.superadmin.Entity.PartnerKYC;

@Repository
public interface PartnerKYCRepo extends JpaRepository<PartnerKYC, Integer> {

PartnerKYC findByPartnerId(Integer partnerId);
    void deleteByPartnerId(Integer partnerId);
    // // Particular KYC document
    // Optional<PaymentKYC> findByKycId(Long kycId);

    // // Kisi partner ke documents exist karte hain ya nahi
    // boolean existsByPartnerId(Long partnerId);

    // // Ek partner ke saare KYC documents delete
    // void deleteByPartnerId(Long partnerId);

    // // Kisi specific document type ka KYC
    // Optional<PaymentKYC> findByPartnerIdAndDocumentType(Long partnerId,
    // DocumentType documentType);
}