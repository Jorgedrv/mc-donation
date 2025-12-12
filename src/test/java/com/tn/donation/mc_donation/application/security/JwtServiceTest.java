package com.tn.donation.mc_donation.application.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setup() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret",
                    "testsecretkey123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtService, "expiration", 900000L);
    }

    @Test
    void generateToken_shouldContainUsername() {
        UserDetails userDetails = User.builder()
                .username("usertest@test.com")
                .password("12345")
                .build();

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);

        String username = jwtService.extractEmail(token);

        assertEquals("usertest@test.com", username);
    }

    @Test
    void tokenShouldBeValidForCorrectUser() {
        UserDetails userDetails = User.builder()
                .username("Peter")
                .password("12345")
                .build();

        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(userDetails));
    }

    @Test
    void tokenShouldBeInvalidForDifferentUser() {
        UserDetails userDetails = User.builder()
                .username("Peter")
                .password("12345")
                .build();

        String token = jwtService.generateToken(userDetails);

        UserDetails wrongUser = User.builder()
                .username("John")
                .password("whatever")
                .build();

        assertFalse(jwtService.isTokenValid(wrongUser));
    }

    @Test
    void expiredTokenShouldNotBeValid() {
        jwtService = new JwtService("b1E9f7K2xP4qM0sT8vR6hC3uY5dW1nB8pL4mX7zT0qV9eS3", 1);

        UserDetails userDetails = User.builder()
                .username("usertest@test.com")
                .password("12345")
                .build();

        String token = jwtService.generateToken(userDetails);

        assertThrows(ExpiredJwtException.class, () -> jwtService.extractEmail(token));
    }
}
