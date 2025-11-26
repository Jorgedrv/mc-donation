package com.tn.donation.mc_donation.application.donation;

import com.tn.donation.mc_donation.common.exception.DonationNotFoundException;
import com.tn.donation.mc_donation.domain.model.Donation;
import com.tn.donation.mc_donation.domain.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetDonationsService {

    private final DonationRepository donationRepository;

    public Donation findById(Long id) {
        return donationRepository.findById(id)
                .orElseThrow(() -> new DonationNotFoundException(id));
    }
}
