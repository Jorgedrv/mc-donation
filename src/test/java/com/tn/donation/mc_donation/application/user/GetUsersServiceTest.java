package com.tn.donation.mc_donation.application.user;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUsersServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    GetUsersService getUsersService;

    @Test
    void findById_shouldReturnUser() {
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

        User user = new User(
                id,
                "elon",
                "musk",
                "elonmusk@test.com",
                true,
                UserStatus.ACTIVE,
                Set.of(role)
        );

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        User userResponse = getUsersService.findById(id);

        assertNotNull(userResponse);
        assertEquals(id, userResponse.getId());

        verify(userRepository).findById(id);
    }

    @Test
    void findById_shouldThrowException() {
        Long id = 10L;
        when(userRepository.findById(id))
                .thenThrow(new DonorNotFoundException(id));

        assertThrows(DonorNotFoundException.class,
                () -> getUsersService.findById(id));

        verify(userRepository).findById(id);
    }
}
