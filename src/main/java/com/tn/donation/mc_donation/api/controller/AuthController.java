package com.tn.donation.mc_donation.api.controller;

import com.tn.donation.mc_donation.api.dto.LoginRequest;
import com.tn.donation.mc_donation.api.dto.LoginResponse;
import com.tn.donation.mc_donation.api.dto.RegisterRequest;
import com.tn.donation.mc_donation.api.dto.RegisterResponse;
import com.tn.donation.mc_donation.application.auth.AuthService;
import com.tn.donation.mc_donation.application.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Login", description = "Authentication Login")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final AuthService authService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Operation(
            summary = "Authenticate user",
            description = "Validates the user's credentials and returns a JWT access token."
    )
    @ApiResponse(responseCode = "200", description = "Authentication successful. JWT token returned.")
    @ApiResponse(responseCode = "400", description = "Invalid request payload.")
    @ApiResponse(responseCode = "401", description = "Invalid username or password.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        log.info("Attempting login for user '{}'", request.username());

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(), request.password()
                )
        );

        UserDetails user = (UserDetails) auth.getPrincipal();

        log.info("Login successful for user '{}'", user.getUsername());

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @Operation(
            summary = "Register user",
            description = "Register user and returns a JWT access token."
    )
    @ApiResponse(responseCode = "200", description = "Register successful. User registered and JWT token returned.")
    @ApiResponse(responseCode = "400", description = "Invalid request payload.")
    @ApiResponse(responseCode = "401", description = "Invalid username or password.")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
