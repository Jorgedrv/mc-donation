package com.tn.donation.mc_donation.application.security;

import com.tn.donation.mc_donation.infrastructure.repository.jpa.UserJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.RoleEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity admin = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        String[] roles = admin.getRoles()
                .stream()
                .map(RoleEntity::getName)
                .toArray(String[]::new);

        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .roles(roles)
                .build();
    }
}
