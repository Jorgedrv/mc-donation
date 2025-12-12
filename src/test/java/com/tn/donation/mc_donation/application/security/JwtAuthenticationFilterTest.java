package com.tn.donation.mc_donation.application.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.PrintWriter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    JwtService jwtService;

    @Mock
    UserDetailsService uds;

    @Mock
    FilterChain filterChain;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    PrintWriter writer;

    JwtAuthenticationFilter filter;

    @BeforeEach
    void setup() throws Exception {
        filter = new JwtAuthenticationFilter(jwtService, uds);
        lenient().when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void doFilterInternal_shouldPassThroughWhenNoAuthHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractEmail(any());
    }

    @Test
    void doFilterInternal_shouldPassThroughWhenNoBearerToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("good123");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractEmail(any());
    }

    @Test
    void doFilterInternal_shouldReturnInvalidTokenError() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer bad.token");
        when(jwtService.extractEmail("bad.token")).thenThrow(new JwtException("Invalid"));

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).write(contains("INVALID_TOKEN"));
    }

    @Test
    void doFilterInternal_shouldReturnExpiredTokenError() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer abc123");
        when(jwtService.extractEmail("abc123")).thenThrow(new ExpiredJwtException(null, null, "expired"));

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).write(contains("TOKEN_EXPIRED"));
    }

    @Test
    void shouldNotAuthenticateWhenUsernameIsNull() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(jwtService.extractEmail("token123")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(uds);
    }

    @Test
    void shouldNotAuthenticateWhenTokenIsInvalid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(jwtService.extractEmail("token123")).thenReturn("usertest@test.com");

        UserDetails user = User.withUsername("usertest@test.com")
                .password("123")
                .roles("ADMIN")
                .build();

        when(uds.loadUserByUsername("usertest@test.com")).thenReturn(user);
        when(jwtService.isTokenValid("token123", user)).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldSkipAuthenticationWhenContextAlreadyHasAuth() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer abc123");
        when(jwtService.extractEmail("abc123")).thenReturn("usertest@test.com");

        Authentication existingAuth =
                new UsernamePasswordAuthenticationToken("ExistingUser", null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(existingAuth);

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertEquals("ExistingUser", auth.getName());

        verify(filterChain).doFilter(request, response);

        verifyNoInteractions(uds);
    }

    @Test
    void doFilterInternal_shouldAuthenticateUserWhenTokenValid() throws Exception {
        String email = "usertest@test.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer good123");
        when(jwtService.extractEmail("good123")).thenReturn(email);

        UserDetails user = org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password("123")
                .roles("ADMIN")
                .build();

        when(uds.loadUserByUsername(email)).thenReturn(user);
        when(jwtService.isTokenValid("good123", user)).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(auth);
        assertEquals(email, auth.getName());

        verify(filterChain, times(1)).doFilter(request, response);
    }
}
