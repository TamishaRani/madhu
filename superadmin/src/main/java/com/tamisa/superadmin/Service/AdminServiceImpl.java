package com.tamisa.superadmin.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tamisa.superadmin.Entity.Commission;
import com.tamisa.superadmin.Entity.Partner;
import com.tamisa.superadmin.Entity.PartnerCustomer;
import com.tamisa.superadmin.Entity.Tier;
import com.tamisa.superadmin.Enum.PartnerStatus;
import com.tamisa.superadmin.Repo.CommissionRepo;
import com.tamisa.superadmin.Repo.CustomerRepo;
import com.tamisa.superadmin.Repo.PartnerBankRepo;
import com.tamisa.superadmin.Repo.PartnerKYCRepo;
import com.tamisa.superadmin.Repo.PartnerNdaRepo;
import com.tamisa.superadmin.Repo.PartnerRepo;
import com.tamisa.superadmin.Repo.TierRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminServiceImpl implements ItadminService {

        @Autowired
        public PartnerRepo partnerRepo;

        @Autowired
        private PartnerKYCRepo partnerKYCRepo;
        @Autowired
        private PartnerBankRepo bankRepo;

        @Autowired
        private PartnerNdaRepo partnerNdaRepo;

        @Autowired
        private EmailService emailService;

        @Autowired
        public TierRepo tierRepo;

        @Autowired
        public CustomerRepo customerRepo;

        @Autowired
        public CommissionRepo commissionRepo;

        @Override
        public List<Partner> getAllPartners() {
                return partnerRepo.findAll();
        }

        @Transactional
        @Override
        public Partner approveOrRejectPartner(int id, String status, String tierid, String rejectionReason) {
                Partner partner = partnerRepo.findById(id)
                                .orElseThrow(() -> new RuntimeException("Partner not found"));

                if ("APPROVED".equalsIgnoreCase(status)) {
                        if (tierid == null || tierid.isBlank()) {
                                throw new RuntimeException("Tier ID is required for approval.");
                        }

                        partner.setStatus(PartnerStatus.APPROVED);
                        partner.setApprovedBy(1);

                        List<Tier> tiers = tierRepo.findAll();
                        boolean tierFound = false;
                        for (Tier tier : tiers) {
                                if (tier.getTierName() != null && tier.getTierName().equalsIgnoreCase(tierid)) {
                                        partner.setTierId(tier.getTierId());
                                        partner.setTierName(tier.getTierName());
                                        tierFound = true;
                                        break;
                                }
                        }

                        if (!tierFound) {
                                throw new RuntimeException("Invalid Partner Plan: " + tierid);
                        }

                        String partnerCode = "PARTNER-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                        partner.setPartnerCode(partnerCode);

                        Partner savedPartner = partnerRepo.save(partner);

                        String loginLink = "https://partner.yourcompany.com/login";
                        String body = """
                                        <html>
                                        <body style="font-family:Arial,sans-serif; background:#f4f6f9;padding:20px;">
                                          <div style="max-width:600px;margin:auto; background:#ffffff;border-radius:10px; overflow:hidden;">
                                            <div style="background:#28a745; color:white;padding:20px; text-align:center;">
                                              <h2>Application Approved 🎉</h2>
                                            </div>
                                            <div style="padding:25px;">
                                              <p>Dear <b>%s</b>,</p>
                                              <p>Congratulations! Your Channel Partner application has been approved.</p>
                                              <table style="width:100%%; border-collapse:collapse; background:#f8f9fa;">
                                                <tr>
                                                  <td style="padding:10px;"><b>Partner Code</b></td>
                                                  <td style="padding:10px;">%s</td>
                                                </tr>
                                                <tr>
                                                  <td style="padding:10px;"><b>Partner Plan</b></td>
                                                  <td style="padding:10px;">%s</td>
                                                </tr>
                                              </table>
                                              <br>
                                              <a href="%s" style="background:#007bff; color:white;padding:12px 20px; text-decoration:none; border-radius:5px;">
                                                Login Portal
                                              </a>
                                              <br><br>
                                              <p>Regards,<br><b>Admin Team</b></p>
                                            </div>
                                          </div>
                                        </body>
                                        </html>
                                        """
                                        .formatted(
                                                        savedPartner.getContactPerson(),
                                                        savedPartner.getPartnerCode(),
                                                        savedPartner.getTierName(),
                                                        loginLink);

                        emailService.sendHtmlMail(savedPartner.getEmail(), "Application Approved", body);
                        return savedPartner;
                } else if ("REJECTED".equalsIgnoreCase(status)) {
                        if (rejectionReason == null || rejectionReason.isBlank()) {
                                throw new RuntimeException("Rejection reason is required.");
                        }

                        partner.setStatus(PartnerStatus.REJECTED);
                        partner.setTierId((long) 1); 
                        partner.setTierName(null);
                        partner.setPartnerCode(null);
                        partner.setRejectionReason(rejectionReason);
                        partner.setApprovedBy(1);

                        Partner savedPartner = partnerRepo.save(partner);

                        String body = """
                                        <html>
                                        <body style="font-family:Arial,sans-serif; background:#f4f6f9;padding:20px;">
                                          <div style="max-width:600px;margin:auto; background:#ffffff;border-radius:10px; overflow:hidden;">
                                            <div style="background:#dc3545; color:white;padding:20px; text-align:center;">
                                              <h2>Application Rejected</h2>
                                            </div>
                                            <div style="padding:25px;">
                                              <p>Dear <b>%s</b>,</p>
                                              <p>Thank you for applying as our Channel Partner.</p>
                                              <p>After reviewing your application, we regret to inform you that it has been rejected.</p>
                                              <div style="background:#fff3cd; border-left:5px solid #ffc107; padding:15px;margin:20px 0;">
                                                <b>Feedback from Admin</b>
                                                <hr>
                                                <p>%s</p>
                                              </div>
                                              <p>Please review the above feedback and apply again after making the required changes.</p>
                                              <br>
                                              <p>Regards,<br><b>Admin Team</b></p>
                                            </div>
                                          </div>
                                        </body>
                                        </html>
                                        """
                                        .formatted(savedPartner.getContactPerson(), savedPartner.getRejectionReason());

                        emailService.sendHtmlMail(savedPartner.getEmail(), "Application Rejected", body);
                        return savedPartner;
                } else {
                        throw new RuntimeException("Invalid status. Use APPROVED or REJECTED.");
                }
        }

        @Override
        public Partner getPartnerById(int id) {

                return partnerRepo.findById(id)
                                .orElseThrow(() -> new RuntimeException("Partner not found"));
        }

        @Override
        public void deleteById(int id) {
                partnerRepo.deleteById(id);
        }

        @Override
        public void deleteAll() {
                partnerRepo.deleteAll();
        }

        @Override
        public Partner reviewApplication(Integer id) {

                System.out.println("Received ID = " + id);

                Partner partner = partnerRepo.findById(id)
                                .orElseThrow(() -> new RuntimeException("Partner not found"));

                partner.setStatus(PartnerStatus.IN_REVIEW);
                partner.setModifiedBy(1);

                Partner savedPartner = partnerRepo.save(partner);

                System.out.println(
                                "MAIL GOING TO : "
                                                + savedPartner.getEmail());

                String subject = "Application Under Review";

                String body = """
                                <html>
                                <body
                                style="margin:0;padding:0;background:#f4f6f9;font-family:Arial,sans-serif;">

                                <div style="max-width:650px;margin:30px auto;background:#ffffff;
                                border-radius:10px;overflow:hidden;
                                box-shadow:0 4px 12px rgba(0,0,0,0.15);">

                                <div style="background:#0d6efd;color:white;padding:20px;text-align:center;">
                                <h2 style="margin:0;">Application Under Review</h2>
                                </div>

                                <div style="padding:30px;">

                                <p>Dear <b>%s</b>,</p>

                                <p>
                                Thank you for submitting your Channel Partner application.
                                </p>

                                <p>
                                We are pleased to inform you that your application has been
                                successfully received and is currently
                                <b>under review</b> by our administration team.
                                </p>

                                <div style="
                                background:#eaf4ff;
                                border-left:5px solid #0d6efd;
                                padding:18px;
                                margin:25px 0;
                                border-radius:5px;">

                                <b>Current Status :</b><br><br>

                                Your application is currently being reviewed by our team.
                                Once the verification process is completed, you will receive
                                another email regarding the final decision.

                                </div>

                                <p>
                                We appreciate your patience and thank you for choosing to
                                partner with us.
                                </p>

                                <br>

                                <p>
                                Regards,<br>
                                <b>Admin Team</b>
                                </p>

                                </div>

                                <div style="
                                background:#f8f9fa;
                                text-align:center;
                                padding:15px;
                                color:#777;
                                font-size:13px;">

                                © 2026 BBB Plus. All Rights Reserved.

                                </div>

                                </div>

                                </body>
                                </html>
                                """
                                .formatted(savedPartner.getContactPerson());

                emailService.sendHtmlMail(
                                savedPartner.getEmail(),
                                subject,
                                body);

                return savedPartner;
        }

@Override
public List<PartnerCustomer> getCustomersByPartnerCode(String partnerCode) {

    return customerRepo.findByPartner_PartnerCode(partnerCode);
}
@Override
@Transactional
public PartnerCustomer createCustomer(
        String partnerCode,
        PartnerCustomer customer) {

    
    Partner partner = partnerRepo
            .findByPartnerCode(partnerCode)
            .orElseThrow(() ->
                    new RuntimeException(
                            "Partner not found with code: " + partnerCode));
    customer.setPartnerId(partner.getPartnerId());
    PartnerCustomer savedCustomer = customerRepo.save(customer);
    BigDecimal totalSales =
            subscriptionRepo.getTotalSalesByPartnerId(
                    partner.getPartnerId());

    
    Tier tier = tierRepo
            .findTopBySalesTargetLessThanEqualOrderBySalesTargetDesc(
                    totalSales)
            .orElseThrow(() ->
                    new RuntimeException("No tier found"));

    // 6. Create commission
    Commission commission = new Commission();

    commission.setDate(LocalDate.now());

    commission.setCustomerId(
            savedCustomer.getId());

    commission.setPartnerId(
            partner.getPartnerCode());

    // Level comes from Tier
    commission.setLevel(
            tier.getTierName());

    // Commission percentage comes from Tier
    commission.setCommissionPercent(
            tier.getCommissionPercent());

    commission.setStatus("Unpaid");

    commission.setSettlementDate(null);

    commissionRepo.save(commission);

    return savedCustomer;
}
}