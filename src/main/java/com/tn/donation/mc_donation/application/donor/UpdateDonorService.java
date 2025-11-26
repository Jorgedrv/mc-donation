package com.tn.donation.mc_donation.application.donor;

import com.tn.donation.mc_donation.api.dto.CreateDonorRequest;
import com.tn.donation.mc_donation.common.exception.DonorNotFoundException;
import com.tn.donation.mc_donation.domain.model.Donor;
import com.tn.donation.mc_donation.domain.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateDonorService {

    private final DonorRepository donorRepository;

    public Donor update(Long id, CreateDonorRequest request) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new DonorNotFoundException(id));
        donor.setFullName(request.getFullName());
        donor.setEmail(request.getEmail());
        return donorRepository.save(donor);
    }
}
