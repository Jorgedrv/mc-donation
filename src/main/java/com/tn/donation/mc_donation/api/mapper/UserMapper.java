package com.tn.donation.mc_donation.api.mapper;

import com.tn.donation.mc_donation.api.dto.UserDTO;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserDTO toResponse(UserEntity userEntity) {
        return new UserDTO(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail()
        );
    }
}
