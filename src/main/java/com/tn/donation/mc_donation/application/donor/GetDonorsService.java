package com.tn.donation.mc_donation.application.donor;

import com.tn.donation.mc_donation.common.exception.DonorNotFoundException;
import com.tn.donation.mc_donation.domain.model.Donor;
import com.tn.donation.mc_donation.domain.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetDonorsService {

    private final DonorRepository donorRepository;

    public Donor findById(Long id) {
        return donorRepository.findById(id)
                .orElseThrow(() -> new DonorNotFoundException(id));
    }
}
