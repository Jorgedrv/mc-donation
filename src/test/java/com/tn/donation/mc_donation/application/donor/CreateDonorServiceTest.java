package com.tn.donation.mc_donation.application.donor;

import com.tn.donation.mc_donation.api.dto.CreateDonorRequest;
import com.tn.donation.mc_donation.domain.model.Donor;
import com.tn.donation.mc_donation.domain.repository.DonorRepository;
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
class CreateDonorServiceTest {

    @Mock
    DonorRepository donorRepository;

    @InjectMocks
    CreateDonorService createDonorService;

    @Test
    void create_shouldSaveDonor() {
        Donor donor = new Donor(
                1L,
                "Peter Parker",
                "peterparker@gmail.com"
        );

        CreateDonorRequest request = new CreateDonorRequest(
                "Peter Parker",
                "peterparker@gmail.com"
        );

        when(donorRepository.save(any(Donor.class))).thenReturn(donor);
        Donor response = createDonorService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());

        verify(donorRepository).save(argThat(saved ->
                saved.getFullName().equals("Peter Parker") &&
                        saved.getEmail().equals("peterparker@gmail.com")
        ));
    }
}
