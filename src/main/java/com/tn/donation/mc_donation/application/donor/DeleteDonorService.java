package com.tn.donation.mc_donation.application.donor;

import com.tn.donation.mc_donation.common.exception.CampaignNotFoundException;
import com.tn.donation.mc_donation.domain.model.Donor;
import com.tn.donation.mc_donation.domain.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteDonorService {

    private final DonorRepository donorRepository;

    public void deleteById(Long id) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException(id));
        donorRepository.delete(donor);
    }
}
