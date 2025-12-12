package com.tn.donation.mc_donation.application.campaign;

import com.tn.donation.mc_donation.api.dto.CreateCampaignRequest;
import com.tn.donation.mc_donation.domain.model.Campaign;
import com.tn.donation.mc_donation.domain.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCampaignService {

    private final CampaignRepository campaignRepository;

    public Campaign create(CreateCampaignRequest request) {
        Campaign campaign = new Campaign(
                request.getName(),
                request.getDescription(),
                request.getIcon(),
                request.getStatus()
        );
        return campaignRepository.save(campaign);
    }
}
