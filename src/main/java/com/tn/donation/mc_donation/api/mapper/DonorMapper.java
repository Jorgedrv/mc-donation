package com.tn.donation.mc_donation.api.mapper;

import com.tn.donation.mc_donation.api.dto.DonorResponse;
import com.tn.donation.mc_donation.domain.model.Donor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DonorMapper {

    public static DonorResponse toResponse(Donor donor) {
        return new DonorResponse(
                donor.getId(),
                donor.getFullName(),
                donor.getEmail(),
                donor.getCreatedAt()
        );
    }
}
