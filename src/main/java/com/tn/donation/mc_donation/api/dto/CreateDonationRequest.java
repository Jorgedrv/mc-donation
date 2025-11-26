package com.tn.donation.mc_donation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDonationRequest {

    private Long donorId;
    private Long campaignId;
    private Double amount;
}
