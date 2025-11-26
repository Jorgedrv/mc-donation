package com.tn.donation.mc_donation.application.donor;

import com.tn.donation.mc_donation.domain.model.Donor;
import com.tn.donation.mc_donation.domain.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListDonorsService {

    private final DonorRepository donorRepository;

    public List<Donor> findAll() {
        return donorRepository.findAll();
    }
}
