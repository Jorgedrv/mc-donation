package com.tn.donation.mc_donation.infrastructure.repository.impl;

import com.tn.donation.mc_donation.domain.model.Donation;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.DonationJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.DonationEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.impl.DonationRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonationRepositoryImplTest {

    @Mock
    DonationJpaRepository donationJpaRepository;

    @InjectMocks
    DonationRepositoryImpl donationRepository;

    @Test
    void saveDonation_ShouldReturnSavedDonation() {
        Long donorId = 10L;
        Long campaignId = 100L;
        Instant fixedInstant = Instant.parse("2025-01-01T00:00:00Z");

        Donation donation = new Donation(
                donorId,
                campaignId,
                100.00,
                Instant.parse("2025-01-01T00:00:00Z")
        );

        when(donationJpaRepository.save(any(DonationEntity.class)))
                .thenAnswer(invocation -> {
                    DonationEntity input = invocation.getArgument(0);
                    DonationEntity saved = new DonationEntity();
                    saved.setId(1L);
                    saved.setDonorId(input.getDonorId());
                    saved.setCampaignId(input.getCampaignId());
                    saved.setAmount(input.getAmount());
                    saved.setCreatedAt(input.getCreatedAt());
                    return saved;
                });

        Donation result = donationRepository.save(donation);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(donorId, result.getDonorId());
        assertEquals(campaignId, result.getCampaignId());
        assertEquals(100.00, result.getAmount());
        assertEquals(fixedInstant, result.getCreatedAt());

        verify(donationJpaRepository).save(argThat(saved ->
                saved.getCampaignId().equals(campaignId) &&
                        saved.getDonorId().equals(donorId) &&
                        saved.getAmount() == 100.00
        ));
    }

    @Test
    void findAll_shouldReturnDonations() {
        Long id = 1L;
        Long donorId = 10L;
        Long campaignId = 100L;
        Instant fixedInstant = Instant.parse("2025-01-01T00:00:00Z");

        DonationEntity entity = new DonationEntity();
        entity.setId(id);
        entity.setDonorId(donorId);
        entity.setCampaignId(campaignId);
        entity.setAmount(100.00);
        entity.setCreatedAt(fixedInstant);

        when(donationJpaRepository.findAll()).thenReturn(List.of(entity));

        List<Donation> donations = donationRepository.findAll();

        assertEquals(1, donations.size());
        assertNotNull(donations.get(0));

        Donation donation = donations.get(0);

        assertAll(
                () -> assertEquals(id, donation.getId()),
                () -> assertEquals(donorId, donation.getDonorId()),
                () -> assertEquals(campaignId, donation.getCampaignId()),
                () -> assertEquals(100.00, donation.getAmount()),
                () -> assertEquals(fixedInstant, donation.getCreatedAt())
        );

        verify(donationJpaRepository).findAll();
    }

    @Test
    void findById_shouldReturnDonation() {
        Long id = 1L;
        Long donorId = 10L;
        Long campaignId = 100L;
        Instant fixedInstant = Instant.parse("2025-01-01T00:00:00Z");

        DonationEntity entity = new DonationEntity();
        entity.setId(id);
        entity.setDonorId(donorId);
        entity.setCampaignId(campaignId);
        entity.setAmount(100.00);
        entity.setCreatedAt(fixedInstant);

        when(donationJpaRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<Donation> result = donationRepository.findById(id);

        assertTrue(result.isPresent());
        assertNotNull(result.get());

        Donation donation = result.get();

        assertAll(
                () -> assertEquals(id, donation.getId()),
                () -> assertEquals(donorId, donation.getDonorId()),
                () -> assertEquals(campaignId, donation.getCampaignId()),
                () -> assertEquals(100.00, donation.getAmount()),
                () -> assertEquals(fixedInstant, donation.getCreatedAt())
        );

        verify(donationJpaRepository).findById(id);
    }

    @Test
    void delete_shouldDeleteDonationSuccessfully() {
        Long id = 1L;
        Long donorId = 10L;
        Long campaignId = 100L;

        Donation donation = new Donation(
                id,
                donorId,
                campaignId,
                100.00,
                Instant.parse("2025-01-01T00:00:00Z")
        );

        doNothing().when(donationJpaRepository).deleteById(id);

        donationRepository.delete(donation);

        verify(donationJpaRepository).deleteById(id);
        verifyNoMoreInteractions(donationJpaRepository);
    }
}
