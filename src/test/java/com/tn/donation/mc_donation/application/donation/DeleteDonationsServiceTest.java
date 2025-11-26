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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteDonationsServiceTest {

    @Mock
    DonationRepository donationRepository;

    @InjectMocks
    DeleteDonationService deleteDonationService;

    @Test
    void delete_shouldDeleteDonationSuccessfully() {
        Long id = 1L;

        Donation donation = new Donation(
                1L,
                1L,
                1L,
                100.00,
                Instant.parse("2025-01-01T00:00:00Z")
        );

        when(donationRepository.findById(id))
                .thenReturn(Optional.of(donation));
        doNothing().when(donationRepository).delete(donation);

        deleteDonationService.delete(id);

        verify(donationRepository).findById(id);
        verify(donationRepository).delete(donation);
        verifyNoMoreInteractions(donationRepository);
    }

    @Test
    void delete_shouldThrowNotFoundException() {
        Long id = 10L;

        when(donationRepository.findById(id))
                .thenThrow(new DonationNotFoundException(id));

        assertThrows(DonationNotFoundException.class,
                () -> deleteDonationService.delete(id));

        verify(donationRepository).findById(id);
        verifyNoMoreInteractions(donationRepository);
    }
}
