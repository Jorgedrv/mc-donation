package com.tn.donation.mc_donation.application.auth;

import com.tn.donation.mc_donation.api.dto.LoginResponse;
import com.tn.donation.mc_donation.api.dto.MenuDTO;
import com.tn.donation.mc_donation.api.dto.UserDTO;
import com.tn.donation.mc_donation.api.mapper.MenuMapper;
import com.tn.donation.mc_donation.api.mapper.UserMapper;
import com.tn.donation.mc_donation.application.security.CustomUserDetails;
import com.tn.donation.mc_donation.application.security.JwtService;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.MenuEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final JwtService jwtService;

    public LoginResponse buildLoginResponse(Authentication auth) {
        CustomUserDetails custom = (CustomUserDetails) auth.getPrincipal();
        UserEntity user = custom.user();

        UserDTO userDTO = UserMapper.toResponse(user);

        Set<MenuEntity> menus = user.getRoles().stream()
                .flatMap(role -> role.getMenus().stream())
                .collect(Collectors.toSet());

        List<MenuDTO> menuDTOs = menus.stream()
                .sorted(Comparator.comparing(MenuEntity::getOrderIndex))
                .map(MenuMapper::toResponse)
                .toList();

        String token = jwtService.generateToken(custom);

        return new LoginResponse(
                token,
                userDTO,
                menuDTOs
        );
    }
}
