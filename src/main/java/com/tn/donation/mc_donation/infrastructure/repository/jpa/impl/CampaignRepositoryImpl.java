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

    private final CampaignJpaRepository campaignJpaRepository;

    @Override
    public Campaign save(Campaign campaign) {
        CampaignEntity entity = new CampaignEntity();
        entity.setId(campaign.getId());
        entity.setName(campaign.getName());
        entity.setDescription(campaign.getDescription());
        entity.setIcon(campaign.getIcon());
        entity.setStatus(campaign.getStatus());
        CampaignEntity saved = campaignJpaRepository.save(entity);
        return new Campaign(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getIcon(),
                saved.getStatus()
        );
    }

    @Override
    public List<Campaign> findAll() {
        return campaignJpaRepository.findAll()
                .stream()
                .map(entity -> new Campaign(
                        entity.getId(),
                        entity.getName(),
                        entity.getDescription(),
                        entity.getIcon(),
                        entity.getStatus()
                ))
                .toList();
    }

    @Override
    public Optional<Campaign> findById(Long id) {
        return campaignJpaRepository.findById(id)
                .map(entity -> new Campaign(
                        entity.getId(),
                        entity.getName(),
                        entity.getDescription(),
                        entity.getIcon(),
                        entity.getStatus()
                ));
    }

    @Override
    public void delete(Campaign campaign) {
        campaignJpaRepository.deleteById(campaign.getId());
    }
}
