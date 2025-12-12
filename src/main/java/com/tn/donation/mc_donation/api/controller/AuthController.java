package com.tn.donation.mc_donation.api.controller;

import com.tn.donation.mc_donation.api.dto.LoginRequest;
import com.tn.donation.mc_donation.api.dto.LoginResponse;
import com.tn.donation.mc_donation.api.dto.RegisterRequest;
import com.tn.donation.mc_donation.api.dto.RegisterResponse;
import com.tn.donation.mc_donation.application.auth.AuthService;
import com.tn.donation.mc_donation.application.auth.LoginService;
import com.tn.donation.mc_donation.application.auth.VerificationTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Login", description = "Authentication Login")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final LoginService loginService;
    private final AuthService authService;
    private final VerificationTokenService verificationTokenService;

    @Operation(
            summary = "Authenticate user",
            description = "Validates the user's credentials and returns a JWT access token."
    )
    @ApiResponse(responseCode = "200", description = "Authentication successful. JWT token returned.")
    @ApiResponse(responseCode = "400", description = "Invalid request payload.")
    @ApiResponse(responseCode = "401", description = "Invalid username or password.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username().trim().toLowerCase(), request.password()
                )
        );
        LoginResponse response = loginService.buildLoginResponse(auth);
        return ResponseEntity.ok(response);
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

    @Operation(
            summary = "Verify user email",
            description = "Verifies a user's email based on the verification token."
    )
    @ApiResponse(responseCode = "200", description = "Email verified successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid or expired token.")
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        verificationTokenService.verifyToken(token);
        return ResponseEntity.ok("Email verified successfully. You can now log in.");
    }
}
