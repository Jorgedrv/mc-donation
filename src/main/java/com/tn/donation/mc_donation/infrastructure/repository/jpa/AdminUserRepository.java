package com.tn.donation.mc_donation.infrastructure.repository.jpa;

import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.AdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUserEntity, Long> {
    Optional<AdminUserEntity> findByUsername(String username);
}
