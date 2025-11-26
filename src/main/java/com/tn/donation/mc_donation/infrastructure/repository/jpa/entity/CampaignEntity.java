package com.tn.donation.mc_donation.infrastructure.repository.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "campaings")
@Data
public class CampaignEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
}
