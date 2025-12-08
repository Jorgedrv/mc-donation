package com.tn.donation.mc_donation.infrastructure.repository.jpa;

import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.UserEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenJpaRepository extends JpaRepository<VerificationTokenEntity, Long>  {

    Optional<VerificationTokenEntity> findByToken(String token);

    void deleteByUser(UserEntity user);
}
