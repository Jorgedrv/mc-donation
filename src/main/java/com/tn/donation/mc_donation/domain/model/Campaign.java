package com.tn.donation.mc_donation.domain.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Campaign {

    private Long id;
    private String name;
    private String description;

    public Campaign(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
