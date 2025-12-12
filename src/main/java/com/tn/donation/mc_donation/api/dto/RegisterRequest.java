package com.tn.donation.mc_donation.api.dto;

public record RegisterRequest(
        String name,
        String lastname,
        String email,
        String password
) {}
