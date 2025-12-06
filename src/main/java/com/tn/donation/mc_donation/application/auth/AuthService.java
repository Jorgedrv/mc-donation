package com.tn.donation.mc_donation.application.auth;

import com.tn.donation.mc_donation.api.dto.RegisterRequest;
import com.tn.donation.mc_donation.api.dto.RegisterResponse;
import com.tn.donation.mc_donation.api.mapper.UserDetailsMapper;
import com.tn.donation.mc_donation.application.security.JwtService;
import com.tn.donation.mc_donation.common.exception.EmailAlreadyExistsException;
import com.tn.donation.mc_donation.common.exception.RoleNotFoundException;
import com.tn.donation.mc_donation.common.exception.UserAlreadyExistsException;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.RoleJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.UserJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.RoleEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public RegisterResponse register(RegisterRequest request) {
        if (userJpaRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        if (userJpaRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException(request.username());
        }

        RoleEntity userRole = roleJpaRepository.findByName("USER")
                .orElseThrow(() -> new RoleNotFoundException("USER role not found"));

        UserEntity user = new UserEntity();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.getRoles().add(userRole);

        UserEntity saved = userJpaRepository.save(user);

        String token = jwtService.generateToken(UserDetailsMapper.toUserDetails(saved));

        return new RegisterResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                token,
                "User registered successfully"
        );
    }
}
