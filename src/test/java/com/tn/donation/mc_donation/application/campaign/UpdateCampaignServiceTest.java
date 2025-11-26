package com.tn.donation.mc_donation.application.campaign;

import com.tn.donation.mc_donation.api.dto.CreateCampaignRequest;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCampaignServiceTest {

    @Mock
    CampaignRepository campaignRepository;

    @InjectMocks
    UpdateCampaignService updateCampaignService;

    @Test
    void update_shouldUpdateCampaign() {
        Long id = 1L;
        Campaign existing = new Campaign(
                id,
                "Support Homeless Children",
                "Providing shelter and meals for vulnerable kids."
        );

        CreateCampaignRequest request = new CreateCampaignRequest(
                "Emergency Shelter Program",
                "Providing shelter and meals for vulnerable kids."
        );

        when(campaignRepository.findById(id)).thenReturn(Optional.of(existing));
        when(campaignRepository.save(any(Campaign.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Campaign response = updateCampaignService.update(id, request);

        assertNotNull(response);
        assertEquals("Emergency Shelter Program", response.getName());

        verify(campaignRepository).findById(id);
        verify(campaignRepository).save(argThat(updated ->
                updated.getName().equals("Emergency Shelter Program") &&
                        updated.getDescription().equals("Providing shelter and meals for vulnerable kids.")
        ));
    }

    @Test
    void update_shouldThrowException() {
        Long id = 10L;

        when(campaignRepository.findById(id)).thenThrow(new CampaignNotFoundException(id));

        CreateCampaignRequest request = new CreateCampaignRequest(
                "Emergency Shelter Program",
                "Providing shelter and meals for vulnerable kids."
        );

        assertThrows(CampaignNotFoundException.class,
                () -> updateCampaignService.update(id, request));

        verify(campaignRepository).findById(id);
        verifyNoMoreInteractions(campaignRepository);
    }
}
