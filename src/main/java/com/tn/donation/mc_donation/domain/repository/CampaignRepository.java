package com.tn.donation.mc_donation.domain.repository;

import com.tn.donation.mc_donation.domain.model.Campaign;

import java.util.List;
import java.util.Optional;

public interface CampaignRepository {

    Campaign save(Campaign campaign);
    List<Campaign> findAll();
    Optional<Campaign> findById(Long id);
    void delete(Campaign campaign);
}
