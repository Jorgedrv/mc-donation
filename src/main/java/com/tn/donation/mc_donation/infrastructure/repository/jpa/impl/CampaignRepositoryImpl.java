package com.tn.donation.mc_donation.infrastructure.repository.jpa.impl;

import com.tn.donation.mc_donation.domain.model.Campaign;
import com.tn.donation.mc_donation.domain.repository.CampaignRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.CampaignEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.CampaignJpaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CampaignRepositoryImpl implements CampaignRepository {

    private final CampaignJpaRepository campaignRepository;

    @Override
    public Campaign save(Campaign campaign) {
        CampaignEntity entity = new CampaignEntity();
        entity.setId(campaign.getId());
        entity.setName(campaign.getName());
        entity.setDescription(campaign.getDescription());
        CampaignEntity saved = campaignRepository.save(entity);
        return new Campaign(saved.getId(), saved.getName(), saved.getDescription());
    }

    @Override
    public List<Campaign> findAll() {
        return campaignRepository.findAll()
                .stream()
                .map(entity -> new Campaign(
                        entity.getId(),
                        entity.getName(),
                        entity.getDescription()
                ))
                .toList();
    }

    @Override
    public Optional<Campaign> findById(Long id) {
        return campaignRepository.findById(id)
                .map(entity -> new Campaign(
                        entity.getId(),
                        entity.getName(),
                        entity.getDescription()
                ));
    }

    @Override
    public void delete(Campaign campaign) {
        campaignRepository.deleteById(campaign.getId());
    }
}
