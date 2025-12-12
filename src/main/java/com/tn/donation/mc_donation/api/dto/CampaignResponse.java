package com.tn.donation.mc_donation.api.dto;

import com.tn.donation.mc_donation.common.CampaignStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CampaignResponse {

    private Long id;
    private String name;
    private String description;
    private String icon;
    private CampaignStatus status;
}
