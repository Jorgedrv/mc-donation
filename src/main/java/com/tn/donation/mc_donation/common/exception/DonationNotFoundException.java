package com.tn.donation.mc_donation.common.exception;

public class DonationNotFoundException extends RuntimeException {

    public DonationNotFoundException(Long id) {
        super("Donation not found with id: " + id);
    }
}
