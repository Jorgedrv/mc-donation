package com.tn.donation.mc_donation.domain.repository;

import com.tn.donation.mc_donation.domain.model.Donor;

import java.util.List;
import java.util.Optional;

public interface DonorRepository {

    Donor save(Donor donor);
    List<Donor> findAll();
    Optional<Donor> findById(Long id);
    void delete(Donor donor);
}
