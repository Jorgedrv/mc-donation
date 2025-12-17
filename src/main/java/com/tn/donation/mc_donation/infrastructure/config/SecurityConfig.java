package com.tn.donation.mc_donation.infrastructure.config;

import com.tn.donation.mc_donation.application.security.JwtAuthenticationFilter;
import com.tn.donation.mc_donation.application.security.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final MyUserDetailsService myUserDetailsService;

    private static final String DONATIONS_PATTERN = "/donations/**";
    private static final String CAMPAIGNS_PATTERN = "/campaigns/**";
    private static final String USERS_PATTERN = "/users/**";
    private static final String ADMIN = "ADMIN";

    public SecurityConfig(JwtAuthenticationFilter jwtFilter, MyUserDetailsService myUserDetailsService) {
        this.jwtFilter = jwtFilter;
        this.myUserDetailsService = myUserDetailsService;
    }

    @SuppressWarnings("java:S4502")
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                    // --- PUBLIC ---
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/actuator/health").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                    .requestMatchers(HttpMethod.GET, "/auth/verify").permitAll()

                    .requestMatchers(HttpMethod.GET, CAMPAIGNS_PATTERN).permitAll()
                    .requestMatchers(HttpMethod.GET, DONATIONS_PATTERN).permitAll()
                    .requestMatchers(HttpMethod.POST, DONATIONS_PATTERN).permitAll()

                    // --- PRIVATE (ADMIN ONLY) ---
                    .requestMatchers(HttpMethod.POST, CAMPAIGNS_PATTERN).hasRole(ADMIN)
                    .requestMatchers(HttpMethod.PUT, CAMPAIGNS_PATTERN).hasRole(ADMIN)
                    .requestMatchers(HttpMethod.DELETE, CAMPAIGNS_PATTERN).hasRole(ADMIN)

                    .requestMatchers(HttpMethod.PUT, USERS_PATTERN).hasRole(ADMIN)
                    .requestMatchers(HttpMethod.PATCH, USERS_PATTERN).hasRole(ADMIN)
                    .requestMatchers(HttpMethod.GET, USERS_PATTERN).hasRole(ADMIN)
                    .requestMatchers(HttpMethod.DELETE, USERS_PATTERN).hasRole(ADMIN)
                    .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()

                    .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
