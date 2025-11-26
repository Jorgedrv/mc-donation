package com.tn.donation.mc_donation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DonationResponse {

    private Long id;
    private Long donorId;
    private Long campaignId;
    private Double amount;
    private Instant createdAt;
}
