package com.tn.donation.mc_donation.common.handler;

import com.tn.donation.mc_donation.common.exception.CampaignNotFoundException;
import com.tn.donation.mc_donation.common.exception.DonationNotFoundException;
import com.tn.donation.mc_donation.common.exception.DonorNotFoundException;
import com.tn.donation.mc_donation.common.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CampaignNotFoundException.class)
    public ResponseEntity<String> handleCampaignNotFound(CampaignNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DonorNotFoundException.class)
    public ResponseEntity<String> handleDonorNotFound(DonorNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DonationNotFoundException.class)
    public ResponseEntity<String> handleDonationNotFound(DonationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorResponse(
                        "INVALID_CREDENTIALS",
                        "Username or password is incorrect",
                        Instant.now()
                )
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorResponse(
                        "AUTH_ERROR",
                        ex.getMessage(),
                        Instant.now()
                )
        );
    }
}
