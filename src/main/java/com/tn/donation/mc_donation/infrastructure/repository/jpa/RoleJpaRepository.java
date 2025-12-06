package com.tn.donation.mc_donation.infrastructure.repository.jpa;

import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(String name);
}
