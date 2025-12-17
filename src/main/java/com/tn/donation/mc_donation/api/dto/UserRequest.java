package com.tn.donation.mc_donation.api.dto;

import com.tn.donation.mc_donation.domain.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String name;
    private String lastname;
    private UserStatus status;
    private Set<RoleRequest> roles;
    private Boolean enabled;
}
