package com.tn.donation.mc_donation.api.mapper;

import com.tn.donation.mc_donation.api.dto.DonationResponse;
import com.tn.donation.mc_donation.domain.model.Donation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DonationMapper {

    public static DonationResponse toResponse(Donation donor) {
        return new DonationResponse(
                donor.getId(),
                donor.getDonorId(),
                donor.getCampaignId(),
                donor.getAmount(),
                donor.getCreatedAt()
        );
    }
}
