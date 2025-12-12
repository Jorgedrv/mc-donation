package com.tn.donation.mc_donation.application.security;

import com.tn.donation.mc_donation.infrastructure.repository.jpa.UserJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.MenuEntity;
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
        String name = "Peter";
        String lastname = "Parker";
        String email = "test@test.com";

        MenuEntity menu = new MenuEntity();
        menu.setId(1L);
        menu.setOrderIndex(1);
        menu.setName("Dashboard");
        menu.setPath("/dashboard");
        menu.setIcon("iconoir:home");

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName(name);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPassword("123456");
        user.setRoles(Set.of(new RoleEntity(null, "ADMIN", Set.of(menu))));

        when(userJpaRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        UserDetails userDetails = myUserDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("123456", userDetails.getPassword());

        assertTrue(
                userDetails.getAuthorities()
                        .stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
        );

        verify(userJpaRepository).findByEmail(email);
    }

    @Test
    void loadUserByUsername_shouldReturnNotFoundException() {
        String username = "unknown@test.com";

        when(userJpaRepository.findByEmail(username))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> myUserDetailsService.loadUserByUsername(username));

        verify(userJpaRepository).findByEmail(username);
    }
}
