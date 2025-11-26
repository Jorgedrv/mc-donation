package com.tn.donation.mc_donation.application.campaign;

import com.tn.donation.mc_donation.common.exception.CampaignNotFoundException;
import com.tn.donation.mc_donation.domain.model.Campaign;
import com.tn.donation.mc_donation.domain.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCampaignService {

    private final CampaignRepository campaignRepository;

    public void delete(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException(id));
        campaignRepository.delete(campaign);
    }
}
