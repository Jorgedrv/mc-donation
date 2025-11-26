package com.tn.donation.mc_donation.application.donation;

import com.tn.donation.mc_donation.common.exception.DonationNotFoundException;
import com.tn.donation.mc_donation.domain.model.Donation;
import com.tn.donation.mc_donation.domain.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteDonationService {

    private final DonationRepository donationRepository;

    public void delete(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new DonationNotFoundException(id));
        donationRepository.delete(donation);
    }
}
