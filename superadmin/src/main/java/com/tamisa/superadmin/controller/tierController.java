package com.tamisa.superadmin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tamisa.superadmin.Entity.Tier;

import com.tamisa.superadmin.Service.ItTier;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/tblPartnerTier")
public class tierController {

    @Autowired
    private ItTier ItTier;

    @PostMapping("/saveTier")
    public ResponseEntity<?> savetier(
            @RequestBody Tier tier) {

        Map<String, Object> res = new HashMap<>();

    
       if (tier.getTierId() == null || tier.getTierId() == 0L) {

          Tier newtier = ItTier.saveTier(tier);
            res.put("status", true);
            res.put("data", newtier);
            res.put("message", "tier saved successfully");

            return new ResponseEntity<>(
                    res,
                    HttpStatus.CREATED);

        } else {

            Tier updatedtTier =
                    ItTier.updateTier(tier,tier.getTierId() );

            res.put("status", true);
            res.put("data", updatedtTier);
            res.put("message", "tier updated successfully");

            return new ResponseEntity<>(
                    res,
                    HttpStatus.OK);
        }
    }
     @GetMapping("/getAllTiers")
    public ResponseEntity<?> getAllTiers() {

        Map<String, Object> res = new HashMap<>();

       List<Tier> TierList = ItTier.getAllTiers();

    res.put("status", true);

         res.put("data", TierList);
        res.put("message", "All Tiers fetched successfully");

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
@GetMapping("/getTierById/{tierId}")
public ResponseEntity<?> getTierById(@PathVariable Long tierId) {

    Map<String, Object> res = new HashMap<>();

    Tier tier = ItTier.getTierById(tierId);

    res.put("status", true);
    res.put("data", tier);
    res.put("message", "Tier fetched successfully");

    return new ResponseEntity<>(res, HttpStatus.OK);
}
}