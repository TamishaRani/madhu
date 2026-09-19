package com.tamisa.superadmin.dto;

import lombok.Data;

@Data
public class TierDescriptionDTO {

    private String paymentCycle;
    private String commission;
    private Boolean brandRights;
    private Boolean partnerPortalAccess;
    private Boolean getLeads;
    private Boolean websiteVisibility;
    private String marketing;
    private String training;
    private Boolean technicalSupport;
    private Boolean serviceLevelAgreement;

}