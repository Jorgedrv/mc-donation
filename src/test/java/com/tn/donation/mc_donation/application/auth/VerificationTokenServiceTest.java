package com.tn.donation.mc_donation.application.auth;

import com.tn.donation.mc_donation.common.exception.*;
import com.tn.donation.mc_donation.domain.enums.UserStatus;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.UserJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.VerificationTokenJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.UserEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.VerificationTokenEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificationTokenServiceTest {

    @Mock
    UserJpaRepository userRepository;

    @Mock
    VerificationTokenJpaRepository verificationTokenRepository;

    @InjectMocks
    VerificationTokenService verificationTokenService;

    @Test
    void createToken_shouldReturnValidToken() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Elon");
        userEntity.setName("Musk");
        userEntity.setEmail("testuser@test.com");
        userEntity.setStatus(UserStatus.PENDING);

        doNothing().when(verificationTokenRepository).deleteByUser(userEntity);

        VerificationTokenEntity entity = VerificationTokenEntity.builder()
                .token("a81b2d73-5de4-4495-b851-98ab3c69ddb6")
                .user(userEntity)
                .expiresAt(LocalDateTime.now())
                .confirmedAt(null)
                .build();

        when(verificationTokenRepository.save(any(VerificationTokenEntity.class)))
                .thenReturn(entity);

        String token = verificationTokenService.createToken(userEntity);

        assertNotNull(token);

        verify(verificationTokenRepository).deleteByUser(userEntity);
        verify(verificationTokenRepository).save(any(VerificationTokenEntity.class));
    }

    @Test
    void verifyToken_shouldActivateUserAndConfirmToken() {
        String token = "a81b2d73-5de4-4495-b851-98ab3c69ddb6";

        UserEntity userEntity = new UserEntity();
        userEntity.setName("Elon");
        userEntity.setLastname("Musk");
        userEntity.setEmail("testuser@test.com");
        userEntity.setPassword("testuser12345");
        userEntity.setStatus(UserStatus.PENDING);

        VerificationTokenEntity entity = VerificationTokenEntity.builder()
                .token(token)
                .user(userEntity)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .confirmedAt(null)
                .build();

        when(verificationTokenRepository.findByToken(token))
                .thenReturn(Optional.of(entity));

        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(userEntity);

        when(verificationTokenRepository.save(any(VerificationTokenEntity.class)))
                .thenReturn(entity);

        LocalDateTime beforeVerification = LocalDateTime.now();

        verificationTokenService.verifyToken(token);

        verify(verificationTokenRepository).findByToken(token);

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals(UserStatus.ACTIVE, userCaptor.getValue().getStatus());

        ArgumentCaptor<VerificationTokenEntity> tokenCaptor =
                ArgumentCaptor.forClass(VerificationTokenEntity.class);
        verify(verificationTokenRepository).save(tokenCaptor.capture());

        LocalDateTime confirmedAt = tokenCaptor.getValue().getConfirmedAt();

        assertNotNull(confirmedAt);
        assertTrue(confirmedAt.isAfter(beforeVerification.minusSeconds(2)));
    }

    @Test
    void verifyToken_shouldThrowInvalidTokenException() {
        String token = "a81b2d73-5de4-4495-b851-98ab3c69ddb6";

        when(verificationTokenRepository.findByToken(token))
                .thenThrow(new InvalidTokenException("Simulated Failure"));

        assertThrows(InvalidTokenException.class,
                () -> verificationTokenService.verifyToken(token));

        verify(verificationTokenRepository).findByToken(token);
        verifyNoInteractions(userRepository);
    }

    @Test
    void verifyToken_shouldThrowTokenExpiredException() {
        String token = "a81b2d73-5de4-4495-b851-98ab3c69ddb6";

        UserEntity userEntity = new UserEntity();
        userEntity.setName("Elon");
        userEntity.setLastname("Musk");
        userEntity.setEmail("testuser@test.com");
        userEntity.setPassword("testuser12345");
        userEntity.setStatus(UserStatus.PENDING);

        VerificationTokenEntity entity = VerificationTokenEntity.builder()
                .token(token)
                .user(userEntity)
                .expiresAt(LocalDateTime.now().minusMinutes(5))
                .confirmedAt(null)
                .build();

        when(verificationTokenRepository.findByToken(token))
                .thenReturn(Optional.of(entity));

        assertThrows(TokenExpiredException.class,
                () -> verificationTokenService.verifyToken(token));

        verify(verificationTokenRepository).findByToken(token);
        verifyNoInteractions(userRepository);
    }
}
