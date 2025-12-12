package com.tn.donation.mc_donation.infrastructure.repository.jpa;

import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuJpaRepository extends JpaRepository<MenuEntity, Long>  {
}
