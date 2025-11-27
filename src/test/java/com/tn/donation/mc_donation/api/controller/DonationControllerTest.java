package com.tn.donation.mc_donation.api.controller;

import com.tn.donation.mc_donation.api.dto.CreateDonationRequest;
import com.tn.donation.mc_donation.api.dto.DonationResponse;
import com.tn.donation.mc_donation.application.donation.*;
import com.tn.donation.mc_donation.domain.model.Donation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DonationControllerTest {

    @Mock
    CreateDonationService createDonationService;

    @Mock
    ListDonationsService listDonationsService;

    @Mock
    GetDonationsService getDonationsService;

    @Mock
    UpdateDonationService updateDonationService;

    @Mock
    DeleteDonationService deleteDonationService;

    @InjectMocks
    DonationController donationController;

    @Test
    void createDonation_WhenValidRequest_ReturnsCreatedStatusAndResponse() {
        Long id = 1L;
        Long donorId = 10L;
        Long campaignId = 100L;
        Instant fixedInstant = Instant.parse("2024-01-01T00:00:00Z");
        Donation donation = new Donation(id, donorId, campaignId, 100.00, fixedInstant);

        CreateDonationRequest request = new CreateDonationRequest(id, id, 100.00);

        when(createDonationService.create(any(CreateDonationRequest.class))).thenReturn(donation);
        ResponseEntity<DonationResponse> responseEntity = donationController.createDonation(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(id, responseEntity.getBody().getId());
        assertEquals(campaignId, responseEntity.getBody().getCampaignId());
        assertEquals(100.00, responseEntity.getBody().getAmount());

        verify(createDonationService).create(any(CreateDonationRequest.class));
    }

    @Test
    void listDonations_WhenDonationsExist_ReturnsOkStatusAndList() {
        Long id = 1L;
        Long donorId = 10L;
        Long campaignId = 100L;
        Instant fixedInstant = Instant.parse("2024-01-01T00:00:00Z");
        Donation donation = new Donation(id, donorId, campaignId, 100.00, fixedInstant);

        when(listDonationsService.findAll()).thenReturn(List.of(donation));

        ResponseEntity<List<DonationResponse>> response = donationController.listDonations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().size());
        assertEquals(id, response.getBody().get(0).getId());

        verify(listDonationsService).findAll();
    }

    @Test
    void getDonation_WhenDonationExists_ReturnsOkStatusAndList() {
        Long id = 1L;
        Long donorId = 10L;
        Long campaignId = 100L;
        Instant fixedInstant = Instant.parse("2024-01-01T00:00:00Z");
        Donation donation = new Donation(id, donorId, campaignId, 100.00, fixedInstant);

        when(getDonationsService.findById(id)).thenReturn(donation);

        ResponseEntity<DonationResponse> response = donationController.getDonation(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());

        verify(getDonationsService).findById(id);
    }

    @Test
    void updateDonation_WhenValidRequest_ReturnsOkStatusAndUpdatedResponse() {
        Long id = 1L;
        Long donorId = 10L;
        Long campaignId = 100L;
        Double amount = 200.00;
        Instant fixedInstant = Instant.parse("2024-01-01T00:00:00Z");

        Donation donation = new Donation(id, donorId, campaignId, amount, fixedInstant);

        CreateDonationRequest request = new CreateDonationRequest(donorId, campaignId, amount);

        when(updateDonationService.update(id, request)).thenReturn(donation);

        ResponseEntity<DonationResponse> entity = donationController.updateDonation(id, request);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertNotNull(entity.getBody());
        assertEquals(id, entity.getBody().getId());
        assertEquals(amount, entity.getBody().getAmount());

        verify(updateDonationService).update(eq(id), any(CreateDonationRequest.class));
    }

    @Test
    void deleteDonation_WhenDonationExists_ReturnsNoContentStatus() {
        Long id = 1L;

        ResponseEntity<Void> responseEntity = donationController.deleteDonation(id);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(deleteDonationService).delete(id);
    }
}
