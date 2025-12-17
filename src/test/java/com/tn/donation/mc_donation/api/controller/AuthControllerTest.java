package com.tn.donation.mc_donation.api.controller;

import com.tn.donation.mc_donation.api.dto.*;
import com.tn.donation.mc_donation.application.auth.AuthService;
import com.tn.donation.mc_donation.application.auth.LoginService;
import com.tn.donation.mc_donation.application.auth.VerificationTokenService;
import com.tn.donation.mc_donation.domain.enums.UserStatus;
import com.tn.donation.mc_donation.domain.model.User;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.MenuEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.RoleEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    AuthenticationManager authManager;

    @Mock
    Authentication authentication;

    @Mock
    LoginService loginService;

    @Mock
    AuthService authService;

    @Mock
    VerificationTokenService verificationTokenService;

    @InjectMocks
    AuthController authController;

    @Test
    void login_shouldReturnOkResponse() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        LoginResponse fakeResponse = new LoginResponse("FAKE_JWT_TOKEN", null, null);

        when(loginService.buildLoginResponse(authentication))
                .thenReturn(fakeResponse);

        LoginRequest request = new LoginRequest("steve", "12345");

        ResponseEntity<LoginResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("FAKE_JWT_TOKEN", response.getBody().getToken());

        verify(authManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        verify(loginService, times(1))
                .buildLoginResponse(authentication);
    }


    @Test
    void login_shouldThrow_WhenInvalidCredentials() {
        when(authManager.authenticate(any()))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        LoginRequest request = new LoginRequest("admin", "wrong");

        assertThrows(
                org.springframework.security.authentication.BadCredentialsException.class,
                () -> authController.login(request)
        );

        verify(authManager, times(1)).authenticate(any());
        verify(loginService, never()).buildLoginResponse(any());
    }

    @Test
    void login_shouldReturn401_whenUserIsPending() {
        LoginRequest request = new LoginRequest("test@mail.com", "12345");

        when(authManager.authenticate(any()))
                .thenThrow(new DisabledException("User is not verified"));

        assertThrows(DisabledException.class, () -> authController.login(request));

        verifyNoInteractions(loginService);
    }

    @Test
    void register_shouldReturnUserAndOkResponse() {
        String name = "Elon";
        String lastname = "Musk";
        String email = "admin@test.com";

        RegisterRequest request = new RegisterRequest(
                name,
                lastname,
                email,
                "admin1234"
        );

        RegisterResponse response = new RegisterResponse(
                1L,
                name,
                lastname,
                email,
                "User registered successfully"
        );

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(response);

        ResponseEntity<RegisterResponse> result = authController.register(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().id());
        assertEquals(name, result.getBody().name());
        assertEquals(lastname, result.getBody().lastname());
        assertEquals(email, result.getBody().email());
        assertEquals("User registered successfully", result.getBody().message());

        verify(authService).register(any(RegisterRequest.class));
    }

    @Test
    void verifyEmail_shouldReturnSuccessMessage() {
        String message = "Email verified successfully. You can now log in.";

        UserEntity userEntity = new UserEntity();
        userEntity.setName("Elon");
        userEntity.setLastname("Musk");
        userEntity.setEmail("testuser@test.com");
        userEntity.setStatus(UserStatus.PENDING);

        String token = "a81b2d73-5de4-4495-b851-98ab3c69ddb6";

        doNothing().when(verificationTokenService).verifyToken(token);

        ResponseEntity<String> response = authController.verifyEmail(token);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void me_shouldReturnOk_whenAuthenticationExists() {
        Long id = 1L;

        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setId(id);
        menuEntity.setIcon("\uD83D\uDCCA");
        menuEntity.setPath("/dashboard");
        menuEntity.setOrderIndex(1);
        menuEntity.setName("Dashboard");

        RoleEntity role = new RoleEntity();
        role.setId(id);
        role.setName("ADMIN");
        role.setMenus(Set.of(menuEntity));

        User user = new User(
                id,
                "elon",
                "musk",
                "elonmusk@test.com",
                true,
                UserStatus.ACTIVE,
                Set.of(role)
        );

        when(authentication.getPrincipal()).thenReturn(user);

        ResponseEntity<UserResponse> response = authController.me(authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
