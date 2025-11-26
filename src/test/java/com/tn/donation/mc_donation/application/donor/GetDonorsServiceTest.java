package com.tn.donation.mc_donation.application.donor;

import com.tn.donation.mc_donation.common.exception.DonorNotFoundException;
import com.tn.donation.mc_donation.domain.model.Donor;
import com.tn.donation.mc_donation.domain.repository.DonorRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetDonorsServiceTest {

    @Mock
    DonorRepository donorRepository;

    @InjectMocks
    GetDonorsService getDonorsService;

    @Test
    void findById_shouldReturnCampaign() {
        Long id = 1L;
        Donor donor = new Donor(
                id,
                "Peter Parker",
                "peterparker@gmail.com",
                Instant.parse("2025-01-01T00:00:00Z")
        );

        when(donorRepository.findById(id)).thenReturn(Optional.of(donor));
        Donor donorResponse = getDonorsService.findById(id);

        assertNotNull(donorResponse);
        assertEquals(id, donorResponse.getId());

        verify(donorRepository).findById(id);
    }

    @Test
    void findById_shouldThrowException() {
        Long id = 10L;
        when(donorRepository.findById(id))
                .thenThrow(new DonorNotFoundException(id));

        assertThrows(DonorNotFoundException.class,
                () -> donorRepository.findById(id));

        verify(donorRepository).findById(id);
    }
}
