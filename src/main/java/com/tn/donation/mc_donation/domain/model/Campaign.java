package com.tn.donation.mc_donation.domain.model;

import com.tn.donation.mc_donation.common.CampaignStatus;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Campaign {

    private Long id;
    private String name;
    private String description;
    private String icon;
    private CampaignStatus status;

    public Campaign(String name, String description, String icon, CampaignStatus status) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.status = (status != null ? status : CampaignStatus.ACTIVE);
    }
}
