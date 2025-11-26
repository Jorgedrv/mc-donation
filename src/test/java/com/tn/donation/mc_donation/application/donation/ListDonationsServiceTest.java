package com.tn.donation.mc_donation.application.donation;

import com.tn.donation.mc_donation.domain.model.Donation;
import com.tn.donation.mc_donation.domain.repository.DonationRepository;
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
class ListDonationsServiceTest {

    @Mock
    DonationRepository donationRepository;

    @InjectMocks
    ListDonationsService listDonationsService;

    @Test
    void findAll_shouldReturnDonations() {
        Long id = 1L;
        Instant fixedInstant = Instant.parse("2025-01-01T00:00:00Z");

        List<Donation> donations = List.of(new Donation(
                id,
                id,
                id,
                100.00,
                fixedInstant
        ));

        when(donationRepository.findAll()).thenReturn(donations);

        List<Donation> list = listDonationsService.findAll();

        assertEquals(1L, list.size());
        assertEquals(id, list.get(0).getId());

        verify(donationRepository).findAll();
    }
}
