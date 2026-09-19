package com.tamisa.superadmin.Service;

import java.util.List;


import com.tamisa.superadmin.Entity.Tier;

public interface ItTier {
 public Tier saveTier(Tier tier);

 
  Tier updateTier(Tier tier, Long id);
  List<Tier> getAllTiers(); 
 Tier getTierById(Long tierId);
}
