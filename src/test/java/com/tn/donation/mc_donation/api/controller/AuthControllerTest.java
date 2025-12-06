package com.tn.donation.mc_donation.api.controller;

import com.tn.donation.mc_donation.api.dto.LoginRequest;
import com.tn.donation.mc_donation.api.dto.LoginResponse;
import com.tn.donation.mc_donation.api.dto.RegisterRequest;
import com.tn.donation.mc_donation.api.dto.RegisterResponse;
import com.tn.donation.mc_donation.application.auth.AuthService;
import com.tn.donation.mc_donation.application.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    AuthenticationManager authManager;

    @Mock
    Authentication authentication;

    @Mock
    JwtService jwtService;

    @Mock
    AuthService authService;

    @InjectMocks
    AuthController authController;

    @Test
    void login_shouldReturnOkResponse() {
        User userDetails = new User(
                "admin",
                "password",
                java.util.Collections.emptyList()
        );

        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtService.generateToken(userDetails)).thenReturn("FAKE_JWT_TOKEN");

        LoginRequest request = new LoginRequest("steve", "12345");

        ResponseEntity<LoginResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("FAKE_JWT_TOKEN", response.getBody().token());

        verify(authManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1))
                .generateToken(userDetails);
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
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void register_shouldReturnUserAndOkResponse() {
        RegisterRequest request = new RegisterRequest(
                "admin",
                "admin@test.com",
                "password"
        );

        RegisterResponse response = new RegisterResponse(
                1L,
                "admin",
                "admin@test.com",
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb3JnZWRyd.me2qJBFhSIZtvPEYmrKdJmbIPKtt0RVgb",
                "User registered successfully"
        );

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(response);

        ResponseEntity<RegisterResponse> result = authController.register(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().id());
        assertEquals("admin", result.getBody().username());
        assertEquals("admin@test.com", result.getBody().email());
        assertEquals("User registered successfully", result.getBody().message());

        verify(authService).register(any(RegisterRequest.class));
    }
}
