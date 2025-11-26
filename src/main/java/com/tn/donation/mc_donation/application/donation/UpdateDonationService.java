package com.tn.donation.mc_donation.application.donation;

import com.tn.donation.mc_donation.api.dto.CreateDonationRequest;
import com.tn.donation.mc_donation.common.exception.DonationNotFoundException;
import com.tn.donation.mc_donation.domain.model.Donation;
import com.tn.donation.mc_donation.domain.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateDonationService {

    private final DonationRepository donationRepository;

    public Donation update(Long id, CreateDonationRequest request) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new DonationNotFoundException(id));
        donation.setDonorId(request.getDonorId());
        donation.setCampaignId(request.getCampaignId());
        donation.setAmount(request.getAmount());
        return donationRepository.save(donation);
    }
}
