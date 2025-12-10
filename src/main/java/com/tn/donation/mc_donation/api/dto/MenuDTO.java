package com.tn.donation.mc_donation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuDTO {

    private Long id;
    private String name;
    private String path;
    private String icon;
    private Integer orderIndex;
}

