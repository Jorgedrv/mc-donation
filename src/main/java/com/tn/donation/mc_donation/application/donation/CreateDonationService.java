package com.tn.donation.mc_donation.application.donation;

import com.tn.donation.mc_donation.api.dto.CreateDonationRequest;
import com.tn.donation.mc_donation.domain.model.Donation;
import com.tn.donation.mc_donation.domain.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CreateDonationService {

    private final DonationRepository donationRepository;

    public Donation create(CreateDonationRequest request) {
        Donation donation = new Donation(
                request.getDonorId(),
                request.getDonorId(),
                request.getAmount(),
                Instant.now()
        );
        return donationRepository.save(donation);
    }
}
