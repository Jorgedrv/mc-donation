package com.tn.donation.mc_donation.common.exception;

import java.time.Instant;

public record ErrorResponse(
        String error,
        String message,
        Instant timestamp
) {}
