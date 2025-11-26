package com.tn.donation.mc_donation.api.controller;

import com.tn.donation.mc_donation.api.dto.CreateDonorRequest;
import com.tn.donation.mc_donation.api.dto.DonorResponse;
import com.tn.donation.mc_donation.api.mapper.DonorMapper;
import com.tn.donation.mc_donation.application.donor.*;
import com.tn.donation.mc_donation.domain.model.Donor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Donors", description = "API for managing donors")
@RestController
@RequestMapping("/donors")
@RequiredArgsConstructor
public class DonorController {

    private final CreateDonorService createDonorService;
    private final ListDonorsService listDonorsService;
    private final GetDonorsService getDonorsService;
    private final UpdateDonorService updateDonorService;
    private final DeleteDonorService deleteDonorService;

    @Operation(
            summary = "Create a donor",
            description = "Registers a new donor in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Donor created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<DonorResponse> createDonor(
            @RequestBody CreateDonorRequest request) {

        Donor donor = createDonorService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(DonorMapper.toResponse(donor));
    }

    @Operation(
            summary = "List all donors",
            description = "Retrieves all registered donors."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Donor list retrieved successfully")
    })
    @GetMapping("/all")
    public ResponseEntity<List<DonorResponse>> listDonors() {
        List<DonorResponse> response = listDonorsService.findAll()
                .stream()
                .map(DonorMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get donor by ID",
            description = "Retrieves donor information using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Donor retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Donor not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DonorResponse> getDonor(@PathVariable Long id) {
        Donor donor = getDonorsService.findById(id);
        return ResponseEntity.ok(DonorMapper.toResponse(donor));
    }

    @Operation(
            summary = "Update a donor",
            description = "Updates the donor's name or email."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Donor updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Donor not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DonorResponse> updateDonor(
            @PathVariable Long id,
            @RequestBody CreateDonorRequest request) {

        Donor updated = updateDonorService.update(id, request);
        return ResponseEntity.ok(DonorMapper.toResponse(updated));
    }

    @Operation(
            summary = "Delete a donor",
            description = "Deletes a donor by ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Donor deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Donor not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonor(@PathVariable Long id) {
        deleteDonorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
