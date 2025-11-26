package com.tn.donation.mc_donation.infrastructure.repository.jpa.impl;

import com.tn.donation.mc_donation.domain.model.Donation;
import com.tn.donation.mc_donation.domain.repository.DonationRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.DonationJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.DonationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DonationRepositoryImpl implements DonationRepository {

    private final DonationJpaRepository donationJpaRepository;

    @Override
    public Donation save(Donation donation) {
        DonationEntity entity = new DonationEntity();
        entity.setId(donation.getId());
        entity.setDonorId(donation.getDonorId());
        entity.setCampaignId(donation.getCampaignId());
        entity.setAmount(donation.getAmount());
        entity.setCreatedAt(donation.getCreatedAt());
        DonationEntity saved = donationJpaRepository.save(entity);
        return new Donation(
                saved.getId(),
                saved.getDonorId(),
                saved.getCampaignId(),
                saved.getAmount(),
                saved.getCreatedAt()
        );
    }

    @Override
    public List<Donation> findAll() {
        return donationJpaRepository.findAll().stream()
                .map(donationEntity -> new Donation(
                        donationEntity.getId(),
                        donationEntity.getDonorId(),
                        donationEntity.getCampaignId(),
                        donationEntity.getAmount(),
                        donationEntity.getCreatedAt()
                )).toList();
    }

    @Override
    public Optional<Donation> findById(Long id) {
        return donationJpaRepository.findById(id)
                .map(entity -> new Donation(
                        entity.getId(),
                        entity.getDonorId(),
                        entity.getCampaignId(),
                        entity.getAmount(),
                        entity.getCreatedAt()
                ));
    }

    @Override
    public void delete(Donation donation) {
        donationJpaRepository.deleteById(donation.getId());
    }
}
