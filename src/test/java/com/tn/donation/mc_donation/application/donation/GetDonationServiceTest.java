package com.tn.donation.mc_donation.application.donation;

import com.tn.donation.mc_donation.common.exception.DonationNotFoundException;
import com.tn.donation.mc_donation.domain.model.Donation;
import com.tn.donation.mc_donation.domain.repository.DonationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetDonationServiceTest {

    @Mock
    DonationRepository donationRepository;

    @InjectMocks
    GetDonationsService getDonationsService;

    @Test
    void findById_shouldReturnDonation() {
        Long id = 1L;
        Instant fixedInstant = Instant.parse("2025-01-01T00:00:00Z");

        Donation donation =new Donation(
                id,
                id,
                id,
                100.00,
                fixedInstant
        );

        when(donationRepository.findById(id)).thenReturn(Optional.of(donation));

        Donation donationResponse = getDonationsService.findById(id);

        assertNotNull(donationResponse);
        assertEquals(id, donationResponse.getId());

        verify(donationRepository).findById(id);
    }

    @Test
    void findById_shouldThrowNotFoundException() {
        Long id = 10L;

        when(donationRepository.findById(id))
                .thenThrow(new DonationNotFoundException(id));

        assertThrows(DonationNotFoundException.class,
                () -> getDonationsService.findById(id));

        verify(donationRepository).findById(anyLong());
    }
}
