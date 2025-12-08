package com.tn.donation.mc_donation.application.auth;

import com.tn.donation.mc_donation.common.exception.InvalidTokenException;
import com.tn.donation.mc_donation.common.exception.TokenExpiredException;
import com.tn.donation.mc_donation.domain.enums.UserStatus;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.UserJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.VerificationTokenJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.UserEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.VerificationTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenJpaRepository verificationTokenRepository;
    private final UserJpaRepository userRepository;

    public String createToken(UserEntity user) {
        verificationTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();

        VerificationTokenEntity entity = VerificationTokenEntity.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .confirmedAt(null)
                .build();

        verificationTokenRepository.save(entity);

        return token;
    }

    public void verifyToken(String token) {
        VerificationTokenEntity verificationTokenEntity = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        if (verificationTokenEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Verification token has expired. Please request a new one.");
        }

        UserEntity user = verificationTokenEntity.getUser();
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        verificationTokenEntity.setConfirmedAt(LocalDateTime.now());
        verificationTokenRepository.save(verificationTokenEntity);
    }
}
