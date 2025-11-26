package com.tn.donation.mc_donation.api.mapper;

import com.tn.donation.mc_donation.api.dto.CampaignResponse;
import com.tn.donation.mc_donation.domain.model.Campaign;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CampaignMapper {

    public static CampaignResponse toResponse(Campaign campaign) {
        return new CampaignResponse(
                campaign.getId(),
                campaign.getName(),
                campaign.getDescription()
        );
    }
}
