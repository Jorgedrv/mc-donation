package com.tn.donation.mc_donation.application.campaign;

import com.tn.donation.mc_donation.domain.model.Campaign;
import com.tn.donation.mc_donation.domain.repository.CampaignRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListCampaignsServiceTest {

    @Mock
    CampaignRepository campaignRepository;

    @InjectMocks
    ListCampaignsService listCampaignsService;

    @Test
    void findAll_shouldReturnCampaigns() {
        Long id = 1L;
        List<Campaign> campaigns = List.of(new Campaign(
                id,
                "Support Homeless Children",
                "Providing shelter and meals for vulnerable kids."
        ));

        when(campaignRepository.findAll()).thenReturn(campaigns);
        List<Campaign> campaignList = listCampaignsService.findAll();

        assertEquals(1, campaignList.size());
        assertEquals(id, campaignList.get(0).getId());

        verify(campaignRepository).findAll();
    }
}
