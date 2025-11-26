package com.tn.donation.mc_donation.infrastructure.repository.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "donations")
@Data
public class DonationEntity {

    @Id
    @GeneratedValue
    private Long id;
    private Long donorId;
    private Long campaignId;
    private Double amount;
    private Instant createdAt;
}
