package com.tn.donation.mc_donation.api.controller;

import com.tn.donation.mc_donation.api.dto.RoleRequest;
import com.tn.donation.mc_donation.api.dto.UserRequest;
import com.tn.donation.mc_donation.api.dto.UserResponse;
import com.tn.donation.mc_donation.application.user.DeleteUserService;
import com.tn.donation.mc_donation.application.user.GetUsersService;
import com.tn.donation.mc_donation.application.user.ListUsersService;
import com.tn.donation.mc_donation.application.user.UpdateUserService;
import com.tn.donation.mc_donation.domain.enums.UserStatus;
import com.tn.donation.mc_donation.domain.model.User;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    ListUsersService listUsersService;

    @Mock
    GetUsersService getUsersService;

    @Mock
    UpdateUserService updateUserService;

    @Mock
    DeleteUserService deleteUserService;

    @InjectMocks
    UserController userController;

    @Test
    void listUsers_WhenDonorsExist_ReturnsOkStatusAndList() {
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

        when(listUsersService.findAll())
                .thenReturn(List.of(user));

        ResponseEntity<List<UserResponse>> response = userController.listUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());

        verify(listUsersService).findAll();
    }

    @Test
    void getUser_WhenUserExist_ReturnsOkStatusAndUser() {
        Long id = 1L;

        User user = new User(
                id,
                "elon",
                "musk",
                "elonmusk@test.com",
                true,
                UserStatus.ACTIVE,
                null
        );

        when(getUsersService.findById(id)).thenReturn(user);

        ResponseEntity<UserResponse> response = userController.getUser(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("elon", response.getBody().getName());
        assertEquals("musk", response.getBody().getLastname());
        assertEquals(UserStatus.ACTIVE, response.getBody().getStatus());

        verify(getUsersService).findById(id);
    }

    @Test
    void updateUser_WhenValidRequest_ReturnsOkStatusAndUpdatedResponse() {
        Long id = 1L;

        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setName("ADMIN");

        UserRequest request = new UserRequest(
                "elon",
                "musk",
                UserStatus.ACTIVE,
                Set.of(roleRequest),
                true
        );
        User user = new User(
                id,
                "elon",
                "musk",
                "elonmusk@test.com",
                true,
                UserStatus.ACTIVE,
                null
        );

        when(updateUserService.update(id, request)).thenReturn(user);
        ResponseEntity<UserResponse> responseEntity = userController.updateUser(id, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("elon", responseEntity.getBody().getName());

        verify(updateUserService).update(eq(id), any(UserRequest.class));
    }

    @Test
    void deleteCampaign_WhenDonorExists_ReturnsNoContentStatus() {
        Long id = 1L;
        ResponseEntity<Void> responseEntity = userController.deleteUser(id);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(deleteUserService).deleteById(id);
    }
}
