package com.tn.donation.mc_donation.application.user;

import com.tn.donation.mc_donation.common.exception.UserNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteUsersServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    DeleteUserService deleteUserService;

    @Test
    void delete_shouldDeleteDonorSuccessfully() {
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
        doNothing().when(userRepository).delete(user);

        deleteUserService.deleteById(id);

        verify(userRepository).findById(id);
        verify(userRepository).delete(user);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void delete_shouldThrowNotFoundException() {
        Long id = 10L;

        when(userRepository.findById(id))
                .thenThrow(new UserNotFoundException(id));

        assertThrows(UserNotFoundException.class,
                () -> deleteUserService.deleteById(id));

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }
}
