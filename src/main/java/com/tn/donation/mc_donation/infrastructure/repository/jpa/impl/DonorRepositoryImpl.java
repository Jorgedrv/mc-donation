package com.tn.donation.mc_donation.infrastructure.repository.jpa.impl;

import com.tn.donation.mc_donation.domain.model.Donor;
import com.tn.donation.mc_donation.domain.repository.DonorRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.DonorJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.DonorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DonorRepositoryImpl implements DonorRepository {

    private final DonorJpaRepository donorJpaRepository;

    @Override
    public Donor save(Donor donor) {
        DonorEntity donorEntity = new DonorEntity();
        donorEntity.setId(donor.getId());
        donorEntity.setFullName(donor.getFullName());
        donorEntity.setEmail(donor.getEmail());
        DonorEntity saved = donorJpaRepository.save(donorEntity);
        return new Donor(saved.getId(), saved.getFullName(), saved.getEmail());
    }

    @Override
    public List<Donor> findAll() {
        List<DonorEntity> donorEntities = donorJpaRepository.findAll();
        return donorEntities.stream()
                .map(entity -> new Donor(
                        entity.getId(),
                        entity.getFullName(),
                        entity.getEmail(),
                        entity.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public Optional<Donor> findById(Long id) {
        return donorJpaRepository.findById(id)
                .map(entity -> new Donor(
                        entity.getId(),
                        entity.getFullName(),
                        entity.getEmail(),
                        entity.getCreatedAt()
                ));
    }

    @Override
    public void delete(Donor donor) {
        donorJpaRepository.deleteById(donor.getId());
    }
}
