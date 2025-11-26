package com.tn.donation.mc_donation.application.donation;

import com.tn.donation.mc_donation.api.dto.CreateDonationRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateDonationsServiceTest {

    @Mock
    DonationRepository donationRepository;

    @InjectMocks
    UpdateDonationService updateDonationService;

    @Test
    void update_shouldUpdateDonation() {
        Long id = 1L;

        CreateDonationRequest request = new CreateDonationRequest(id, id, 200.00);

        Donation existing = new Donation(
                id,
                id,
                id,
                200.00,
                Instant.parse("2025-01-01T00:00:00Z")
        );

        when(donationRepository.findById(id))
                .thenReturn(Optional.of(existing));
        when(donationRepository.save(any(Donation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Donation update = updateDonationService.update(id, request);

        assertNotNull(update);
        assertEquals(200.00, update.getAmount());

        verify(donationRepository).findById(id);
        verify(donationRepository).save(argThat(updated ->
            updated.getId().equals(id) && updated.getAmount() == 200.00
        ));
    }

    @Test
    void update_shouldThrowNotFoundException() {
        Long id = 1L;

        CreateDonationRequest request = new CreateDonationRequest(id, id, 200.00);

        when(donationRepository.findById(id))
                .thenThrow(new DonationNotFoundException((id)));

        assertThrows(DonationNotFoundException.class,
                () -> updateDonationService.update(id, request));

        verify(donationRepository).findById(id);
        verifyNoMoreInteractions(donationRepository);
    }
}
