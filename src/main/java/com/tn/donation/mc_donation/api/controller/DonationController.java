package com.tn.donation.mc_donation.api.controller;

import com.tn.donation.mc_donation.api.dto.CreateDonationRequest;
import com.tn.donation.mc_donation.api.dto.DonationResponse;
import com.tn.donation.mc_donation.api.mapper.DonationMapper;
import com.tn.donation.mc_donation.application.donation.*;
import com.tn.donation.mc_donation.domain.model.Donation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Donations", description = "Donation management API")
@RestController
@RequestMapping("/donations")
@RequiredArgsConstructor
public class DonationController {

    private final CreateDonationService createDonationService;
    private final ListDonationsService listDonationsService;
    private final GetDonationsService getDonationsService;
    private final UpdateDonationService updateDonationService;
    private final DeleteDonationService deleteDonationService;

    @Operation(
            summary = "Create a donation",
            description = "Creates a new donation for a donor and a campaign."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Donation successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Related Donor or Campaign not found")
    })
    @PostMapping
    public ResponseEntity<DonationResponse> createDonation(@RequestBody CreateDonationRequest request) {
        Donation donation = createDonationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DonationMapper.toResponse(donation));
    }

    @Operation(
            summary = "List all donations",
            description = "Retrieves a list of all donations in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Donations retrieved successfully")
    })
    @GetMapping("/all")
    public ResponseEntity<List<DonationResponse>> listDonations() {
        List<DonationResponse> response = listDonationsService.findAll()
                .stream()
                .map(DonationMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get donation by ID",
            description = "Retrieves a single donation by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Donation retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Donation not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DonationResponse> getDonation(@PathVariable Long id) {
        Donation donation = getDonationsService.findById(id);
        return ResponseEntity.ok(DonationMapper.toResponse(donation));
    }

    @Operation(
            summary = "Update an existing donation",
            description = "Updates the donor, campaign, or amount of an existing donation."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Donation successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Donation not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DonationResponse> updateDonation(
            @PathVariable Long id,
            @RequestBody CreateDonationRequest request) {
        Donation donation = updateDonationService.update(id, request);
        return ResponseEntity.ok(DonationMapper.toResponse(donation));
    }

    @Operation(
            summary = "Delete a donation",
            description = "Deletes a donation using its unique ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Donation successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Donation not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable Long id) {
        deleteDonationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
