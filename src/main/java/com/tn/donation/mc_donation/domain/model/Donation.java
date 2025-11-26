package com.tn.donation.mc_donation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Donation {

    private Long id;
    private Long donorId;
    private Long campaignId;
    private Double amount;
    private Instant createdAt;

    public Donation(Long donorId, Long campaignId, Double amount, Instant createdAt) {
        this.donorId = donorId;
        this.campaignId = campaignId;
        this.amount = amount;
        this.createdAt = createdAt;
    }
}
