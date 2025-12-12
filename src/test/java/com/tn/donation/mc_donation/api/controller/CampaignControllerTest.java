package com.tn.donation.mc_donation.api.controller;

import com.tn.donation.mc_donation.api.dto.CampaignResponse;
import com.tn.donation.mc_donation.api.dto.CreateCampaignRequest;
import com.tn.donation.mc_donation.application.campaign.CreateCampaignService;
import com.tn.donation.mc_donation.application.campaign.DeleteCampaignService;
import com.tn.donation.mc_donation.application.campaign.GetCampaignsService;
import com.tn.donation.mc_donation.application.campaign.ListCampaignsService;
import com.tn.donation.mc_donation.application.campaign.UpdateCampaignService;
import com.tn.donation.mc_donation.common.CampaignStatus;
import com.tn.donation.mc_donation.domain.model.Campaign;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CampaignControllerTest {

    @Mock
    CreateCampaignService createCampaignService;

    @Mock
    ListCampaignsService listCampaignsService;

    @Mock
    GetCampaignsService getCampaignsService;

    @Mock
    UpdateCampaignService updateCampaignService;

    @Mock
    DeleteCampaignService deleteCampaignService;

    @InjectMocks
    CampaignController campaignController;

    @Test
    void createCampaign_WhenValidRequest_ReturnsCreatedStatusAndResponse() {
        CreateCampaignRequest request = new CreateCampaignRequest();
        request.setDescription("Support Homeless Children");
        request.setName("Providing shelter and meals for vulnerable kids.");

        Campaign campaign = new Campaign(
                1L,
                "Support Homeless Children",
                "Providing shelter and meals for vulnerable kids.",
                "iconoir:fire-flame",
                null
        );

        when(createCampaignService.create(any(CreateCampaignRequest.class))).thenReturn(campaign);
        ResponseEntity<CampaignResponse> responseEntity = campaignController.createCampaign(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1L, responseEntity.getBody().getId());

        verify(createCampaignService).create(any(CreateCampaignRequest.class));
    }

    @Test
    void listCampaigns_WhenDonorsExist_ReturnsOkStatusAndList() {
        List<Campaign> campaigns = List.of(
                new Campaign(
                        1L,
                        "Support Homeless Children",
                        "Providing shelter and meals for vulnerable kids.",
                        "iconoir:fire-flame",
                        CampaignStatus.ACTIVE
                )
        );
        when(listCampaignsService.findAll()).thenReturn(campaigns);
        ResponseEntity<List<CampaignResponse>> responseEntity = campaignController.listCampaigns();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals(1L, responseEntity.getBody().get(0).getId());

        verify(listCampaignsService).findAll();
    }

    @Test
    void getCampaign_WhenDonorExists_ReturnsOkStatusAndResponse() {
        Long id = 1L;
        Campaign campaign = new Campaign(
                id,
                "Support Homeless Children",
                "Providing shelter and meals for vulnerable kids.",
                "iconoir:fire-flame",
                CampaignStatus.ACTIVE
        );

        when(getCampaignsService.findById(id)).thenReturn(campaign);

        ResponseEntity<CampaignResponse> responseEntity = campaignController.getCampaign(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(id, responseEntity.getBody().getId());

        verify(getCampaignsService).findById(id);
    }

    @Test
    void updateCampaign_WhenValidRequest_ReturnsOkStatusAndUpdatedResponse() {
        Long id = 1L;
        CreateCampaignRequest request = new CreateCampaignRequest(
                "Emergency Shelter Program",
                "Providing shelter and meals for vulnerable kids.",
                "iconoir:fire-flame",
                CampaignStatus.ACTIVE
        );
        Campaign campaign = new Campaign(
                id,
                "Emergency Shelter Program",
                "Providing shelter and meals for vulnerable kids.",
                "iconoir:fire-flame",
                CampaignStatus.ACTIVE
        );

        when(updateCampaignService.update(id, request)).thenReturn(campaign);
        ResponseEntity<CampaignResponse> responseEntity = campaignController.updateCampaign(id, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Emergency Shelter Program", responseEntity.getBody().getName());

        verify(updateCampaignService).update(eq(id), any(CreateCampaignRequest.class));
    }

    @Test
     void deleteCampaign_WhenDonorExists_ReturnsNoContentStatus() {
        Long id = 1L;
        ResponseEntity<Void> responseEntity = campaignController.deleteCampaign(id);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(deleteCampaignService).delete(id);
    }
}
