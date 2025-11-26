package com.tn.donation.mc_donation.application.campaign;

import com.tn.donation.mc_donation.common.exception.CampaignNotFoundException;
import com.tn.donation.mc_donation.domain.model.Campaign;
import com.tn.donation.mc_donation.domain.repository.CampaignRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCampaignsServiceTest {

    @Mock
    CampaignRepository campaignRepository;

    @InjectMocks
    GetCampaignsService getCampaignsService;

    @Test
    void findById_shouldReturnCampaign() {
        Long id = 1L;
        Campaign campaign = new Campaign(
                id,
                "Support Homeless Children",
                "Providing shelter and meals for vulnerable kids."
        );

        when(campaignRepository.findById(id)).thenReturn(Optional.of(campaign));
        Campaign campaignResponse = getCampaignsService.findById(id);

        assertNotNull(campaignResponse);
        assertEquals(id, campaignResponse.getId());

        verify(campaignRepository).findById(id);
    }

    @Test
    void findById_shouldThrowException() {
        Long id = 10L;
        when(campaignRepository.findById(anyLong()))
                .thenThrow(new CampaignNotFoundException(id));

        assertThrows(CampaignNotFoundException.class,
                () -> getCampaignsService.findById(id));

        verify(campaignRepository).findById(anyLong());
    }
}
