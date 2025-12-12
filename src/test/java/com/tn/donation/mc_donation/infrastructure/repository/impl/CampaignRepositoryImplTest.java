package com.tn.donation.mc_donation.infrastructure.repository.impl;

import com.tn.donation.mc_donation.common.CampaignStatus;
import com.tn.donation.mc_donation.domain.model.Campaign;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.CampaignJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.CampaignEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.impl.CampaignRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CampaignRepositoryImplTest {

    @Mock
    CampaignJpaRepository campaignJpaRepository;

    @InjectMocks
    CampaignRepositoryImpl campaignRepository;

    @Test
    void saveCampaign_ShouldReturnSavedCampaign() {
        Campaign campaign = new Campaign(
                "Support Homeless Children",
                "Providing shelter and meals for vulnerable kids.",
                "iconoir:fire-flame",
                null
        );

        when(campaignJpaRepository.save(any(CampaignEntity.class))).thenAnswer(invocation -> {
            CampaignEntity input = invocation.getArgument(0);
            CampaignEntity saved = new CampaignEntity();
            saved.setId(1L);
            saved.setName(input.getName());
            saved.setDescription(input.getDescription());
            saved.setIcon(input.getIcon());
            saved.setStatus(input.getStatus());
            return saved;
        });

        Campaign campaignResponse = campaignRepository.save(campaign);

        assertNotNull(campaignResponse);
        assertEquals(1L, campaignResponse.getId());
        assertEquals("Support Homeless Children", campaignResponse.getName());
        assertEquals("Providing shelter and meals for vulnerable kids.", campaignResponse.getDescription());
        assertEquals("iconoir:fire-flame", campaignResponse.getIcon());
        assertEquals(CampaignStatus.ACTIVE, campaignResponse.getStatus());

        verify(campaignJpaRepository).save(argThat(saved ->
                saved.getName().equals("Support Homeless Children") &&
                        saved.getDescription().equals("Providing shelter and meals for vulnerable kids.") &&
                        saved.getIcon().equals("iconoir:fire-flame") &&
                        saved.getStatus().equals(CampaignStatus.ACTIVE)
        ));
    }

    @Test
    void saveCampaign_ShouldReturnSavedCampaignWithStatus() {
        Campaign campaign = new Campaign(
                "Support Homeless Children",
                "Providing shelter and meals for vulnerable kids.",
                "iconoir:fire-flame",
                CampaignStatus.ACTIVE
        );

        when(campaignJpaRepository.save(any(CampaignEntity.class))).thenAnswer(invocation -> {
            CampaignEntity input = invocation.getArgument(0);
            CampaignEntity saved = new CampaignEntity();
            saved.setId(1L);
            saved.setName(input.getName());
            saved.setDescription(input.getDescription());
            saved.setIcon(input.getIcon());
            saved.setStatus(input.getStatus());
            return saved;
        });

        Campaign campaignResponse = campaignRepository.save(campaign);

        assertNotNull(campaignResponse);
        assertEquals(1L, campaignResponse.getId());
        assertEquals("Support Homeless Children", campaignResponse.getName());
        assertEquals("Providing shelter and meals for vulnerable kids.", campaignResponse.getDescription());
        assertEquals("iconoir:fire-flame", campaignResponse.getIcon());
        assertEquals(CampaignStatus.ACTIVE, campaignResponse.getStatus());

        verify(campaignJpaRepository).save(argThat(saved ->
                saved.getName().equals("Support Homeless Children") &&
                        saved.getDescription().equals("Providing shelter and meals for vulnerable kids.") &&
                        saved.getIcon().equals("iconoir:fire-flame") &&
                        saved.getStatus().equals(CampaignStatus.ACTIVE)
        ));
    }

    @Test
    void findAll_shouldReturnCampaigns() {
        Long id = 1L;

        CampaignEntity entity = new CampaignEntity();
        entity.setId(id);
        entity.setName("Support Homeless Children");
        entity.setDescription("Providing shelter and meals for vulnerable kids.");
        entity.setIcon("iconoir:fire-flame");
        entity.setStatus(CampaignStatus.ACTIVE);

        when(campaignJpaRepository.findAll()).thenReturn(List.of(entity));

        List<Campaign> result = campaignRepository.findAll();

        assertEquals(1, result.size());
        assertNotNull(result.get(0));

        Campaign campaign = result.get(0);

        assertNotNull(campaign);
        assertEquals(1L, campaign.getId());
        assertEquals("Support Homeless Children", campaign.getName());
        assertEquals("Providing shelter and meals for vulnerable kids.", campaign.getDescription());
        assertEquals("iconoir:fire-flame", campaign.getIcon());
        assertEquals(CampaignStatus.ACTIVE, campaign.getStatus());

        verify(campaignJpaRepository).findAll();
    }

    @Test
    void findById_shouldReturnCampaign() {
        Long id = 1L;

        CampaignEntity entity = new CampaignEntity();
        entity.setId(id);
        entity.setName("Support Homeless Children");
        entity.setDescription("Providing shelter and meals for vulnerable kids.");
        entity.setIcon("iconoir:fire-flame");
        entity.setStatus(CampaignStatus.ACTIVE);

        when(campaignJpaRepository.findById(id))
                .thenReturn(Optional.of(entity));

        Optional<Campaign> result = campaignRepository.findById(id);

        assertTrue(result.isPresent());

        Campaign campaign = result.get();

        assertNotNull(campaign);
        assertEquals(1L, campaign.getId());
        assertEquals("Support Homeless Children", campaign.getName());
        assertEquals("Providing shelter and meals for vulnerable kids.", campaign.getDescription());
        assertEquals("iconoir:fire-flame", campaign.getIcon());
        assertEquals(CampaignStatus.ACTIVE, campaign.getStatus());

        verify(campaignJpaRepository).findById(id);
    }

    @Test
    void delete_shouldDeleteCampaignSuccessfully() {
        Long id = 1L;

        Campaign campaign = new Campaign(
                1L,
                "Support Homeless Children",
                "Providing shelter and meals for vulnerable kids.",
                "iconoir:fire-flame",
                CampaignStatus.ACTIVE
        );

        doNothing().when(campaignJpaRepository).deleteById(id);

        campaignRepository.delete(campaign);

        verify(campaignJpaRepository).deleteById(campaign.getId());
        verifyNoMoreInteractions(campaignJpaRepository);
    }
}
