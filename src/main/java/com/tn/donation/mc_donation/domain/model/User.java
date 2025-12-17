package com.tn.donation.mc_donation.domain.model;

import com.tn.donation.mc_donation.domain.enums.UserStatus;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String name;
    private String lastname;
    private String password;
    private String email;
    private Boolean enabled;
    private UserStatus status;
    private Set<RoleEntity> roles;

    public User(Long id, String name, String lastname, String email,
                Boolean enabled, UserStatus status, Set<RoleEntity> roles) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.enabled = enabled;
        this.status = status;
        this.roles = roles;
    }
}
