package com.tn.donation.mc_donation.api.dto;

public record RegisterResponse(
        Long id,
        String name,
        String lastname,
        String email,
        String message
) {}
