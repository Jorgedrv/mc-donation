package com.tn.donation.mc_donation.api.dto;

import com.tn.donation.mc_donation.domain.enums.UserStatus;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String lastname;
    private String email;
    private Boolean enabled;
    private UserStatus status;
    private Set<RoleEntity> roles;
}
