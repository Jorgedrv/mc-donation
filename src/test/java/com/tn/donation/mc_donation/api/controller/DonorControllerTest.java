package com.tn.donation.mc_donation.api.controller;

import com.tn.donation.mc_donation.api.dto.CreateDonorRequest;
import com.tn.donation.mc_donation.api.dto.DonorResponse;
import com.tn.donation.mc_donation.application.donor.*;
import com.tn.donation.mc_donation.domain.model.Donor;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DonorControllerTest {

    @Mock
    CreateDonorService createDonorService;

    @Mock
    ListDonorsService listDonorsService;

    @Mock
    GetDonorsService getDonorsService;

    @Mock
    UpdateDonorService updateDonorService;

    @Mock
    DeleteDonorService deleteDonorService;

    @InjectMocks
    DonorController donorController;

    @Test
    void createDonor_WhenValidRequest_ReturnsCreatedStatusAndResponse() {
        Instant fixedInstant = Instant.parse("2024-01-01T00:00:00Z");
        Donor donor = new Donor(
                1L,
                "Peter Parker",
                "peterparker@gmail.com",
                fixedInstant
        );

        CreateDonorRequest request = new CreateDonorRequest(
                "Peter Parker",
                "peterparker@gmail.com"
        );

        when(createDonorService.create(any(CreateDonorRequest.class))).thenReturn(donor);
        ResponseEntity<DonorResponse> responseEntity = donorController.createDonor(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1L, responseEntity.getBody().getId());

        verify(createDonorService).create(any(CreateDonorRequest.class));
    }

    @Test
    void listDonors_WhenDonorsExist_ReturnsOkStatusAndList() {
        List<Donor> donors = List.of(new Donor(
                1L,
                "Peter Parker",
                "peterparker@gmail.com",
                Instant.parse("2024-01-01T00:00:00Z")
        ));

        when(listDonorsService.findAll()).thenReturn(donors);
        ResponseEntity<List<DonorResponse>> responseEntity = donorController.listDonors();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals(1L, responseEntity.getBody().get(0).getId());

        verify(listDonorsService).findAll();
    }

    @Test
    void getDonor_WhenDonorExists_ReturnsOkStatusAndResponse() {
        Long id = 1L;
        Donor donor = new Donor(
                1L,
                "Peter Parker",
                "peterparker@gmail.com",
                Instant.parse("2024-01-01T00:00:00Z")
        );
        when(getDonorsService.findById(id)).thenReturn(donor);
        ResponseEntity<DonorResponse> responseEntity = donorController.getDonor(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(id, responseEntity.getBody().getId());

        verify(getDonorsService).findById(id);
    }

    @Test
    void updateDonor_WhenValidRequest_ReturnsOkStatusAndUpdatedResponse() {
        Long id = 1L;
        Donor donor = new Donor(
                id,
                "Dare Devil",
                "peterparker@gmail.com",
                Instant.parse("2024-01-01T00:00:00Z")
        );
        CreateDonorRequest request = new CreateDonorRequest(
                "Dare Devil",
                "peterparker@gmail.com"
        );

        when(updateDonorService.update(id, request)).thenReturn(donor);
        ResponseEntity<DonorResponse> responseEntity = donorController.updateDonor(id, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Dare Devil", responseEntity.getBody().getFullName());

        verify(updateDonorService).update(eq(id), any(CreateDonorRequest.class));
    }

    @Test
    void deleteDonor_WhenDonorExists_ReturnsNoContentStatus() {
        Long id = 1L;
        ResponseEntity<Void> responseEntity = donorController.deleteDonor(id);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(deleteDonorService).deleteById(id);
    }
}
