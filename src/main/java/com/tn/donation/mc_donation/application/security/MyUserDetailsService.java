package com.tn.donation.mc_donation.application.security;

import com.tn.donation.mc_donation.infrastructure.repository.jpa.AdminUserRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.AdminUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUserEntity admin = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .roles(admin.getRole()) // ADMIN
                .build();
    }
}
