package com.tamisa.superadmin.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tamisa.superadmin.Entity.Commission;

@Repository
public interface CommissionRepo extends JpaRepository<Commission, Integer> {

}