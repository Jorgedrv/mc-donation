package com.tn.donation.mc_donation.common.exception;

public class DonorNotFoundException extends RuntimeException {

    public DonorNotFoundException(Long id) {
        super("Donor not found with id: " + id);
    }
}
