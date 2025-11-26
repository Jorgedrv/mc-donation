package com.tn.donation.mc_donation.infrastructure.repository.jpa;

import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.DonorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonorJpaRepository extends JpaRepository<DonorEntity, Long> {
}
