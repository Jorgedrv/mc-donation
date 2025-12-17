package com.tn.donation.mc_donation.application.user;

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

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListUsersServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ListUsersService listUsersService;

    @Test
    void findAll_shouldReturnDonors() {
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

        when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> donorList = listUsersService.findAll();

        assertEquals(1, donorList.size());
        assertEquals(id, donorList.get(0).getId());

        verify(userRepository).findAll();
    }
}
