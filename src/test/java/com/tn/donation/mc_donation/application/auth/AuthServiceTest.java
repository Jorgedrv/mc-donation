package com.tn.donation.mc_donation.application.auth;

import com.tn.donation.mc_donation.api.dto.RegisterRequest;
import com.tn.donation.mc_donation.api.dto.RegisterResponse;
import com.tn.donation.mc_donation.common.exception.EmailAlreadyExistsException;
import com.tn.donation.mc_donation.common.exception.RoleNotFoundException;
import com.tn.donation.mc_donation.domain.enums.UserStatus;
import com.tn.donation.mc_donation.infrastructure.messaging.EmailService;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.RoleJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.UserJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.MenuEntity;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserJpaRepository userJpaRepository;

    @Mock
    RoleJpaRepository roleJpaRepository;

    @Mock
    PasswordEncoder encoder;

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
                new RegisterRequest("Elon", "Musk", email, "1234");

        when(userJpaRepository.existsByEmail("test@mail.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> authService.register(request));

        verify(userJpaRepository).existsByEmail("test@mail.com");
        verify(roleJpaRepository, never()).findByName(anyString());
        verify(userJpaRepository, never()).save(any());
    }

    @Test
    void register_notExistByUsernameShouldThrowException() {
        String email = "test@mail.com";

        RegisterRequest request =
                new RegisterRequest("Elon", "Musk", email, "1234");

        when(userJpaRepository.existsByEmail("test@mail.com")).thenReturn(false);

        assertThrows(RoleNotFoundException.class,
                () -> authService.register(request));

        verify(userJpaRepository, times(1)).existsByEmail(email);
        verify(userJpaRepository, never()).save(any());
    }

    @Test
    void register_roleFindByNameShouldThrowException() {
        String email = "test@mail.com";
        String name = "Elon";
        String lastname = "Musk";

        RegisterRequest request =
                new RegisterRequest(name, lastname, email, "1234");

        when(userJpaRepository.existsByEmail(email)).thenReturn(false);

        when(roleJpaRepository.findByName(anyString()))
                .thenThrow(new RoleNotFoundException("USER"));

        assertThrows(RoleNotFoundException.class,
                () -> authService.register(request));

        verify(userJpaRepository, times(1)).existsByEmail(email);
        verify(userJpaRepository, never()).save(any());
    }

    @Test
    void register_shouldSaveUserSuccessfully() {
        String email = "test@mail.com";
        String name = "Elon";
        String lastname = "Musk";

        RegisterRequest request =
                new RegisterRequest(name, lastname, email, "1234");

        String token = "a81b2d73-5de4-4495-b851-98ab3c69ddb6";

        when(userJpaRepository.existsByEmail("test@mail.com")).thenReturn(false);

        MenuEntity menu = new MenuEntity();
        menu.setId(1L);
        menu.setOrderIndex(1);
        menu.setName("Dashboard");
        menu.setPath("/dashboard");
        menu.setIcon("iconoir:home");

        RoleEntity role = new RoleEntity(1L, "USER", Set.of(menu));
        when(roleJpaRepository.findByName("USER")).thenReturn(Optional.of(role));

        UserEntity saved = new UserEntity();
        saved.setId(1L);
        saved.setPassword("1234");
        saved.setName(name);
        saved.setLastname(lastname);
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
        assertEquals("Elon", response.name());
        assertEquals("Musk", response.lastname());
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
