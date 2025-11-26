package com.tn.donation.mc_donation.common.exception;

public class CampaignNotFoundException extends RuntimeException {

    public CampaignNotFoundException(Long id) {
        super("Campaign not found with id: " + id);
    }
}
