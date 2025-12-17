package com.tn.donation.mc_donation.application.user;

import com.tn.donation.mc_donation.api.dto.RoleRequest;
import com.tn.donation.mc_donation.api.dto.UserRequest;
import com.tn.donation.mc_donation.common.exception.DonorNotFoundException;
import com.tn.donation.mc_donation.domain.enums.UserStatus;
import com.tn.donation.mc_donation.domain.model.User;
import com.tn.donation.mc_donation.domain.repository.UserRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.MenuEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.RoleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUsersServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UpdateUserService updateUserService;

    @Test
    void update_shouldUpdateUser() {
        Long id = 1L;

        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setId(id);
        menuEntity.setIcon("\uD83D\uDCCA");
        menuEntity.setPath("/dashboard");
        menuEntity.setOrderIndex(1);
        menuEntity.setName("Dashboard");

        RoleEntity role = new RoleEntity();
        role.setId(id);
        role.setName("ADMIN");
        role.setMenus(Set.of(menuEntity));

        User existing = new User(
                id,
                "elon",
                "musk",
                "elonmusk@test.com",
                true,
                UserStatus.ACTIVE,
                Set.of(role)
        );

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setName("ADMIN");

        UserRequest request = new UserRequest(
                "elon",
                "musk",
                UserStatus.ACTIVE,
                Set.of(roleRequest),
                Boolean.TRUE
        );

        User userResponse = updateUserService.update(id, request);

        assertNotNull(userResponse);
        assertEquals("elonmusk@test.com", userResponse.getEmail());

        verify(userRepository).findById(id);
        verify(userRepository).save(argThat(updated ->
                updated.getId().equals(id) &&
                        updated.getName().equals("elon") &&
                        updated.getLastname().equals("musk") &&
                        updated.getEnabled().equals(Boolean.TRUE)
        ));
    }

    @Test
    void update_shouldThrowNotFoundException() {
        Long id = 10L;
        when(userRepository.findById(id))
                .thenThrow(new DonorNotFoundException(id));

        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setName("ADMIN");

        UserRequest request = new UserRequest(
                "elon",
                "musk",
                UserStatus.ACTIVE,
                Set.of(roleRequest),
                Boolean.TRUE
        );

        assertThrows(DonorNotFoundException.class, () -> updateUserService.update(id, request));

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }
}
