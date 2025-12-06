package com.tn.donation.mc_donation.api.dto;

public record RegisterRequest(
        String username,
        String email,
        String password
) {}
