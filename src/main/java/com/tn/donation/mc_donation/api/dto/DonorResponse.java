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
public class DonorResponse {

    private Long id;
    private String fullName;
    private String email;
    private Instant createdAt;
}
