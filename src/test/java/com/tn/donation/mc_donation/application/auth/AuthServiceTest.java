package com.tn.donation.mc_donation.application.auth;

import com.tn.donation.mc_donation.api.dto.RegisterRequest;
import com.tn.donation.mc_donation.api.dto.RegisterResponse;
import com.tn.donation.mc_donation.application.security.JwtService;
import com.tn.donation.mc_donation.common.exception.EmailAlreadyExistsException;
import com.tn.donation.mc_donation.common.exception.RoleNotFoundException;
import com.tn.donation.mc_donation.common.exception.UserAlreadyExistsException;
import com.tn.donation.mc_donation.domain.enums.UserStatus;
import com.tn.donation.mc_donation.infrastructure.messaging.EmailService;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.RoleJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.UserJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.RoleEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserJpaRepository userJpaRepository;

    @Mock
    RoleJpaRepository roleJpaRepository;

    @Mock
    PasswordEncoder encoder;

    @Mock
    JwtService jwtService;

    @Mock
    VerificationTokenService verificationTokenService;

    @Mock
    EmailService emailService;

    @InjectMocks
    AuthService authService;

    @Test
    void register_notExistByEmailShouldThrowException() {
        String email = "test@mail.com";

        RegisterRequest request =
                new RegisterRequest("testuser", email, "1234");

        when(userJpaRepository.existsByEmail("test@mail.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> authService.register(request));

        verify(userJpaRepository).existsByEmail("test@mail.com");
        verify(userJpaRepository, never()).existsByUsername(anyString());
        verify(roleJpaRepository, never()).findByName(anyString());
        verify(userJpaRepository, never()).save(any());
    }

    @Test
    void register_notExistByUsernameShouldThrowException() {
        String email = "test@mail.com";
        String username = "testuser";

        RegisterRequest request =
                new RegisterRequest(username, email, "1234");

        when(userJpaRepository.existsByEmail("test@mail.com")).thenReturn(false);

        when(userJpaRepository.existsByUsername(username)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> authService.register(request));

        verify(userJpaRepository, times(1)).existsByEmail(email);
        verify(userJpaRepository).existsByUsername(username);
        verify(roleJpaRepository, never()).findByName(anyString());
        verify(userJpaRepository, never()).save(any());
    }

    @Test
    void register_roleFindByNameShouldThrowException() {
        RegisterRequest request =
                new RegisterRequest("testuser", "testuser@test.com", "1234");

        when(userJpaRepository.existsByEmail("testuser@test.com")).thenReturn(false);

        when(userJpaRepository.existsByUsername("testuser")).thenReturn(false);

        when(roleJpaRepository.findByName(anyString()))
                .thenThrow(new RoleNotFoundException("USER"));

        assertThrows(RoleNotFoundException.class,
                () -> authService.register(request));

        verify(userJpaRepository, times(1)).existsByEmail("testuser@test.com");
        verify(userJpaRepository, times(1)).existsByUsername("testuser");
        verify(userJpaRepository, never()).save(any());
    }

    @Test
    void register_shouldSaveUserSuccessfully() {
        RegisterRequest request =
                new RegisterRequest("testuser", "test@mail.com", "1234");

        String token = "a81b2d73-5de4-4495-b851-98ab3c69ddb6";

        when(userJpaRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(userJpaRepository.existsByUsername("testuser")).thenReturn(false);

        RoleEntity role = new RoleEntity(1L, "USER");
        when(roleJpaRepository.findByName("USER")).thenReturn(Optional.of(role));

        UserEntity saved = new UserEntity();
        saved.setId(1L);
        saved.setPassword("1234");
        saved.setUsername("testuser");
        saved.setEmail("test@mail.com");
        saved.setStatus(UserStatus.PENDING);

        when(encoder.encode("1234")).thenReturn("hashed-password");

        when(userJpaRepository.save(any(UserEntity.class))).thenReturn(saved);

        when(verificationTokenService.createToken(any(UserEntity.class)))
                .thenReturn(token);

        doNothing().when(emailService).sendVerificationEmail("test@mail.com", token);

        RegisterResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("testuser", response.username());
        assertEquals("test@mail.com", response.email());
        assertEquals("User registered successfully. Please check your email to verify your account.",
                response.message());

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userJpaRepository).save(userCaptor.capture());
        assertEquals(UserStatus.PENDING, userCaptor.getValue().getStatus());

        verify(encoder).encode("1234");
        verify(userJpaRepository).save(any(UserEntity.class));
        verify(emailService).sendVerificationEmail("test@mail.com", token);
    }
}
