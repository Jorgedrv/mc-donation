package com.tn.donation.mc_donation.api.mapper;

import com.tn.donation.mc_donation.api.dto.UserDTO;
import com.tn.donation.mc_donation.api.dto.UserResponse;
import com.tn.donation.mc_donation.domain.enums.UserStatus;
import com.tn.donation.mc_donation.domain.model.User;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserDTO toResponse(UserEntity userEntity) {
        return new UserDTO(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getLastname(),
                userEntity.getEmail()
        );
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getLastname(),
                user.getEmail(),
                user.getEnabled(),
                user.getStatus(),
                user.getRoles()
        );
    }
}
