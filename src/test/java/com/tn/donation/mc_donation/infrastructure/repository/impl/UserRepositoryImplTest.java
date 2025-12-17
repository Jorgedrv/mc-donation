package com.tn.donation.mc_donation.infrastructure.repository.impl;

import com.tn.donation.mc_donation.domain.enums.UserStatus;
import com.tn.donation.mc_donation.domain.model.User;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.UserJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.*;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.impl.UserRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    UserJpaRepository userJpaRepository;

    @InjectMocks
    UserRepositoryImpl userRepository;

    @Test
    void save_ShouldPersistAndReturnUser() {
        Long id = 1L;

        RoleEntity role = new RoleEntity();
        role.setId(1L);
        role.setName("ADMIN");

        User existing = new User(
                id,
                "elon",
                "musk",
                "elonmusk@test.com",
                true,
                UserStatus.ACTIVE,
                Set.of(role)
        );

        when(userJpaRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> {
                    UserEntity input = invocation.getArgument(0);
                    input.setId(1L);
                    return input;
                });

        User result = userRepository.save(existing);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("elon", result.getName());
        assertEquals("musk", result.getLastname());
        assertEquals(UserStatus.ACTIVE, result.getStatus());
        assertTrue(result.getEnabled());
        assertEquals(1, result.getRoles().size());

        assertNull(result.getEmail());

        verify(userJpaRepository).save(argThat(saved ->
                saved.getName().equals("elon") &&
                        saved.getLastname().equals("musk") &&
                        saved.getStatus() == UserStatus.ACTIVE &&
                        saved.getRoles().size() == 1
        ));
    }

    @Test
    void findAll_ShouldReturnListOfUsers() {
        RoleEntity role = new RoleEntity();
        role.setId(1L);
        role.setName("ADMIN");

        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setName("elon");
        entity.setLastname("musk");
        entity.setEmail("elon@test.com");
        entity.setEnabled(true);
        entity.setStatus(UserStatus.ACTIVE);
        entity.setRoles(Set.of(role));

        when(userJpaRepository.findAll()).thenReturn(List.of(entity));

        List<User> result = userRepository.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());

        User user = result.get(0);
        assertEquals(1L, user.getId());
        assertEquals("elon", user.getName());
        assertEquals("musk", user.getLastname());
        assertEquals("elon@test.com", user.getEmail());
        assertTrue(user.getEnabled());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertEquals(1, user.getRoles().size());

        verify(userJpaRepository).findAll();
    }

    @Test
    void findById_ShouldReturnUser_WhenExists() {
        Long id = 1L;

        RoleEntity role = new RoleEntity();
        role.setId(1L);
        role.setName("ADMIN");

        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setName("elon");
        entity.setLastname("musk");
        entity.setEmail("elon@test.com");
        entity.setEnabled(true);
        entity.setStatus(UserStatus.ACTIVE);
        entity.setRoles(Set.of(role));

        when(userJpaRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<User> result = userRepository.findById(id);

        assertTrue(result.isPresent());

        User user = result.get();
        assertEquals(id, user.getId());
        assertEquals("elon", user.getName());
        assertEquals("musk", user.getLastname());
        assertEquals("elon@test.com", user.getEmail());
        assertTrue(user.getEnabled());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertEquals(1, user.getRoles().size());

        verify(userJpaRepository).findById(id);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Long id = 99L;

        when(userJpaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<User> result = userRepository.findById(id);

        assertTrue(result.isEmpty());

        verify(userJpaRepository).findById(id);
    }

    @Test
    void delete_ShouldDeleteUserById() {
        User user = new User(
                1L,
                "elon",
                "musk",
                "elon@test.com",
                true,
                UserStatus.ACTIVE,
                Set.of()
        );

        userRepository.delete(user);

        verify(userJpaRepository).deleteById(1L);
    }
}
