package com.tn.donation.mc_donation.application.donor;

import com.tn.donation.mc_donation.api.dto.CreateDonorRequest;
import com.tn.donation.mc_donation.domain.model.Donor;
import com.tn.donation.mc_donation.domain.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CreateDonorService {

    private final DonorRepository donorRepository;

    public Donor create(CreateDonorRequest request) {
        Donor donor = new Donor(request.getFullName(), request.getEmail());
        return donorRepository.save(donor);
    }
}
