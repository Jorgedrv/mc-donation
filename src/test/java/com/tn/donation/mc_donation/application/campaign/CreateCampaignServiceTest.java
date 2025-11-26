package com.tn.donation.mc_donation.application.campaign;

import com.tn.donation.mc_donation.api.dto.CreateCampaignRequest;
import com.tn.donation.mc_donation.domain.model.Campaign;
import com.tn.donation.mc_donation.domain.repository.CampaignRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCampaignServiceTest {

    @Mock
    CampaignRepository campaignRepository;

    @InjectMocks
    CreateCampaignService createCampaignService;

    @Test
    void create_shouldSaveCampaign() {
        CreateCampaignRequest request = new CreateCampaignRequest(
                "Support Homeless Children",
                "Providing shelter and meals for vulnerable kids."
        );

        when(campaignRepository.save(any(Campaign.class))).thenAnswer(invocation -> {
            Campaign c = invocation.getArgument(0);
            return new Campaign(1L, c.getName(), c.getDescription());
        });
        Campaign response = createCampaignService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());

        verify(campaignRepository).save(argThat(saved ->
                saved.getName().equals("Support Homeless Children") &&
                        saved.getDescription().equals("Providing shelter and meals for vulnerable kids.")
        ));
    }
}
