package com.tn.donation.mc_donation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Donor {

    private Long id;
    private String fullName;
    private String email;
    private Instant createdAt;

    public Donor (Long id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    public Donor (String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }
}
