package com.tn.donation.mc_donation.domain.repository;

import com.tn.donation.mc_donation.domain.model.Donation;

import java.util.List;
import java.util.Optional;

public interface DonationRepository {

    Donation save(Donation donation);
    List<Donation> findAll();
    Optional<Donation> findById(Long id);
    void delete(Donation donor);
}
