package com.tn.donation.mc_donation.application.donor;

import com.tn.donation.mc_donation.domain.model.Donor;
import com.tn.donation.mc_donation.domain.repository.DonorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListDonorsServiceTest {

    @Mock
    DonorRepository donorRepository;

    @InjectMocks
    ListDonorsService listDonorsService;

    @Test
    void findAll_shouldReturnDonors() {
        Long id = 1L;
        List<Donor> donors = List.of(new Donor(
                id,
                "Peter Parker",
                "peterparker@gmail.com",
                Instant.parse("2025-01-01T00:00:00Z")
        ));

        when(donorRepository.findAll()).thenReturn(donors);
        List<Donor> donorList = listDonorsService.findAll();

        assertEquals(1, donorList.size());
        assertEquals(id, donorList.get(0).getId());

        verify(donorRepository).findAll();
    }
}
