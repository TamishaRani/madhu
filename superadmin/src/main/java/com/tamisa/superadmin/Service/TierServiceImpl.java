package com.tamisa.superadmin.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.tamisa.superadmin.Entity.Tier;

import com.tamisa.superadmin.Repo.TierRepo;
@Service
public class TierServiceImpl implements ItTier {
    @Autowired
    public TierRepo tierRepo;

    @Override
public Tier saveTier(Tier tier) {
    return tierRepo.save(tier);
}
   @Override
public Tier updateTier(Tier tier, Long id) {

    Tier existingTier = tierRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Tier not found with ID : " + id));

    existingTier.setTierCode(tier.getTierCode());
    existingTier.setTierName(tier.getTierName());
    existingTier.setDescription(tier.getDescription());
    existingTier.setMinimumSales(tier.getMinimumSales());
    existingTier.setMinimumRenewal(tier.getMinimumRenewal());

    existingTier.setSaleCommission(tier.getSaleCommission());
    existingTier.setRenewalCommission(tier.getRenewalCommission());
    existingTier.setUpgradeCommission(tier.getUpgradeCommission());
    existingTier.setReferralCommission(tier.getReferralCommission());

    existingTier.setPrioritySupport(tier.getPrioritySupport());
    existingTier.setDedicatedManager(tier.getDedicatedManager());

    existingTier.setStatus(tier.getStatus());

    existingTier.setModifiedBy(tier.getModifiedBy());
    existingTier.setModifiedDate(LocalDateTime.now());

    return tierRepo.save(existingTier);
}
 @Override
    public List<Tier> getAllTiers() {
        return tierRepo.findAll();
    }

     @Override
    public Tier getTierById(Long tierId) {

        return tierRepo.findById(tierId)
                .orElseThrow(() -> new RuntimeException("Tier not found with Id : " + tierId));

    }
}
