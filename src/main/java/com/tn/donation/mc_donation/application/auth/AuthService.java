package com.tn.donation.mc_donation.application.auth;

import com.tn.donation.mc_donation.api.dto.RegisterRequest;
import com.tn.donation.mc_donation.api.dto.RegisterResponse;
import com.tn.donation.mc_donation.common.exception.EmailAlreadyExistsException;
import com.tn.donation.mc_donation.common.exception.RoleNotFoundException;
import com.tn.donation.mc_donation.common.exception.UserAlreadyExistsException;
import com.tn.donation.mc_donation.domain.enums.UserStatus;
import com.tn.donation.mc_donation.infrastructure.messaging.EmailService;
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
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

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
        user.setStatus(UserStatus.PENDING);

        UserEntity saved = userJpaRepository.save(user);

        String verificationToken = verificationTokenService.createToken(saved);

        emailService.sendVerificationEmail(saved.getEmail(), verificationToken);

        return new RegisterResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                "User registered successfully. Please check your email to verify your account."
        );
    }
}
