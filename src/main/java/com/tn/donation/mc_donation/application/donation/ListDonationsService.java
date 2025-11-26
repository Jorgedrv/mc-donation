package com.tn.donation.mc_donation.application.donation;

import com.tn.donation.mc_donation.domain.model.Donation;
import com.tn.donation.mc_donation.domain.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListDonationsService {

    private final DonationRepository donationRepository;

    public List<Donation> findAll() {
        return donationRepository.findAll();
    }
}
