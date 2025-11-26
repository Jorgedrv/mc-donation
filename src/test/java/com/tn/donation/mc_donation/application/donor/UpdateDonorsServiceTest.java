package com.tn.donation.mc_donation.application.donor;

import com.tn.donation.mc_donation.api.dto.CreateDonorRequest;
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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateDonorsServiceTest {

    @Mock
    DonorRepository donorRepository;

    @InjectMocks
    UpdateDonorService updateDonorService;

    @Test
    void update_shouldUpdateCampaign() {
        Long id = 1L;
        Instant fixedInstant = Instant.parse("2025-01-01T00:00:00Z");
        CreateDonorRequest request = new CreateDonorRequest(
                "Peter Parker",
                "peterparker2025@gmail.com"
        );

        Donor existing = new Donor(
                1L,
                "Peter Parker",
                "peterparker@gmail.com",
                fixedInstant
        );

        when(donorRepository.findById(id)).thenReturn(Optional.of(existing));
        when(donorRepository.save(any(Donor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Donor donorResponse = updateDonorService.update(id, request);

        assertNotNull(donorResponse);
        assertEquals("peterparker2025@gmail.com", donorResponse.getEmail());

        verify(donorRepository).findById(id);
        verify(donorRepository).save(argThat(updated ->
                updated.getId().equals(id) &&
                        updated.getFullName().equals("Peter Parker") &&
                        updated.getEmail().equals("peterparker2025@gmail.com") &&
                        updated.getCreatedAt().equals(fixedInstant)
        ));
    }

    @Test
    void update_shouldThrowNotFoundException() {
        Long id = 10L;
        when(donorRepository.findById(id))
                .thenThrow(new DonorNotFoundException(id));

        CreateDonorRequest request = new CreateDonorRequest(
                "Peter Parker",
                "peterparker2025@gmail.com"
        );

        assertThrows(DonorNotFoundException.class, () -> updateDonorService.update(id, request));

        verify(donorRepository).findById(id);
        verifyNoMoreInteractions(donorRepository);
    }
}
