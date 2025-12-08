package com.tn.donation.mc_donation.api.dto;

public record RegisterResponse(
        Long id,
        String username,
        String email,
        String message
) {}
