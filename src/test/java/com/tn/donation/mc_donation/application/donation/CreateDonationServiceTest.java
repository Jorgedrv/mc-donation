package com.tn.donation.mc_donation.application.donation;

import com.tn.donation.mc_donation.api.dto.CreateDonationRequest;
import com.tn.donation.mc_donation.domain.model.Donation;
import com.tn.donation.mc_donation.domain.repository.DonationRepository;
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
class CreateDonationServiceTest {

    @Mock
    DonationRepository donationRepository;

    @InjectMocks
    CreateDonationService createDonationService;

    @Test
    void create_shouldSaveDonation() {
        Long id = 1L;

        CreateDonationRequest request = new CreateDonationRequest(id, id, 100.00);

        when(donationRepository.save(any(Donation.class))).thenAnswer(invocation -> {
            Donation d = invocation.getArgument(0);
            return new Donation(
                    1L,
                    d.getCampaignId(),
                    d.getDonorId(),
                    d.getAmount(),
                    d.getCreatedAt()
            );
        });

        Donation response = createDonationService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());

        verify(donationRepository).save(argThat(saved ->
                saved.getDonorId().equals(id) &&
                        saved.getCampaignId().equals(id) &&
                        saved.getAmount() == 100.00
        ));
    }
}
