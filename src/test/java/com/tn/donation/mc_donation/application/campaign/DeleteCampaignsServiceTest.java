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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCampaignsServiceTest {

    @Mock
    CampaignRepository campaignRepository;

    @InjectMocks
    DeleteCampaignService deleteCampaignService;

    @Test
    void delete_shouldDeleteCampaignSuccessfully() {
        Long id = 1L;
        Campaign existing = new Campaign(
                id,
                "Support Homeless Children",
                "Providing shelter and meals for vulnerable kids."
        );

        when(campaignRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(campaignRepository).delete(existing);

        deleteCampaignService.delete(id);

        verify(campaignRepository).findById(id);
        verify(campaignRepository).delete(existing);
        verifyNoMoreInteractions(campaignRepository);
    }

    @Test
    void delete_shouldThrowNotFoundException() {
        Long id = 10L;

        when(campaignRepository.findById(id))
                .thenThrow(new CampaignNotFoundException(id));

        assertThrows(CampaignNotFoundException.class,
                () -> deleteCampaignService.delete(id));

        verify(campaignRepository).findById(id);
        verifyNoMoreInteractions(campaignRepository);
    }
}
