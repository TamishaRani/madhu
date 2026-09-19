package com.tamisa.superadmin.Repo;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tamisa.superadmin.Entity.Tier;

public interface TierRepo extends JpaRepository<Tier, Long> {
  Optional<Tier> findByTierNameIgnoreCase(String tierName);


//     // Tier findTopByCommissionAmountLessThanEqualOrderByCommissionAmountDesc(
//     //         Double commissionAmount);
//    @Query("""
// SELECT COALESCE(SUM(s.price),0)
// FROM Subscription s
// WHERE s.partnerId=:partnerId
// AND YEAR(s.startDate)=:year
// AND MONTH(s.startDate)=:month
// """)
// Double getMonthlySales(Integer partnerId,
//                        Integer year,
//                        Integer month);


// @Query("""
// SELECT COALESCE(SUM(s.price),0)
// FROM Subscription s
// WHERE s.partnerId=:partnerId
// AND YEAR(s.startDate)=:year
// """)
// Double getYearlySales(Integer partnerId,
//                       Integer year);



// @Query("""
// SELECT COALESCE(SUM(s.price),0)
// FROM Subscription s
// WHERE s.partnerId=:partnerId
// AND s.startDate BETWEEN :startDate AND :endDate
// """)
// Double getQuarterlySales(Integer partnerId,
//                          LocalDate startDate,
//                          LocalDate endDate);
                         
                         
                         
}