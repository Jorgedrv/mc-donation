package com.tn.donation.mc_donation.api.controller;

import com.tn.donation.mc_donation.api.dto.CampaignResponse;
import com.tn.donation.mc_donation.api.dto.CreateCampaignRequest;
import com.tn.donation.mc_donation.api.mapper.CampaignMapper;
import com.tn.donation.mc_donation.application.campaign.*;
import com.tn.donation.mc_donation.domain.model.Campaign;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Campaigns", description = "Campaign management API")
@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CreateCampaignService createCampaignService;
    private final ListCampaignsService listCampaignsService;
    private final GetCampaignsService getCampaignsService;
    private final UpdateCampaignService updateCampaignService;
    private final DeleteCampaignService deleteCampaignService;

    @Operation(
            summary = "Create a campaign",
            description = "Creates a new fundraising campaign."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Campaign successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @PostMapping
    public ResponseEntity<CampaignResponse> createCampaign(
            @RequestBody CreateCampaignRequest request) {

        Campaign campaign = createCampaignService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CampaignMapper.toResponse(campaign));
    }

    @Operation(
            summary = "List all campaigns",
            description = "Retrieves all fundraising campaigns."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Campaign list retrieved successfully")
    })
    @GetMapping("/all")
    public ResponseEntity<List<CampaignResponse>> listCampaigns() {
        List<CampaignResponse> response = listCampaignsService.findAll()
                .stream()
                .map(CampaignMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get campaign by ID",
            description = "Retrieves a campaign using its unique ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Campaign retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Campaign not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponse> getCampaign(@PathVariable Long id) {
        Campaign campaign = getCampaignsService.findById(id);
        return ResponseEntity.ok(CampaignMapper.toResponse(campaign));
    }

    @Operation(
            summary = "Update a campaign",
            description = "Updates the name or description of an existing campaign."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Campaign updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Campaign not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CampaignResponse> updateCampaign(
            @PathVariable Long id,
            @RequestBody CreateCampaignRequest request) {

        Campaign updated = updateCampaignService.update(id, request);
        return ResponseEntity.ok(CampaignMapper.toResponse(updated));
    }

    @Operation(
            summary = "Delete a campaign",
            description = "Deletes a campaign using its unique ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Campaign successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Campaign not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        deleteCampaignService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
