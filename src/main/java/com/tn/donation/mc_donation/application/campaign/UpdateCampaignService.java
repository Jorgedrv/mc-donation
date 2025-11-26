package com.tn.donation.mc_donation.application.campaign;

import com.tn.donation.mc_donation.api.dto.CreateCampaignRequest;
import com.tn.donation.mc_donation.common.exception.CampaignNotFoundException;
import com.tn.donation.mc_donation.domain.model.Campaign;
import com.tn.donation.mc_donation.domain.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCampaignService {

    private final CampaignRepository campaignRepository;

    public Campaign update(Long id, CreateCampaignRequest request) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException(id));
        campaign.setName(request.getName());
        campaign.setDescription(request.getDescription());
        return campaignRepository.save(campaign);
    }
}
