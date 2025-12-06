package com.tn.donation.mc_donation.application.security;

import com.tn.donation.mc_donation.infrastructure.repository.jpa.UserJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.RoleEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    MyUserDetailsService myUserDetailsService;

    @Test
    void loadUserByUsername_shouldReturnUser() {
        String username = "peterparker";

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword("123456");
        user.setRoles(Set.of(new RoleEntity(null, "ADMIN")));

        when(userJpaRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("123456", userDetails.getPassword());

        assertTrue(
                userDetails.getAuthorities()
                        .stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
        );

        verify(userJpaRepository).findByUsername(username);
    }

    @Test
    void loadUserByUsername_shouldReturnNotFoundException() {
        String username = "unknown";

        when(userJpaRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> myUserDetailsService.loadUserByUsername(username));

        verify(userJpaRepository).findByUsername(username);
    }
}
