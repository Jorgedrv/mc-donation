package com.tn.donation.mc_donation.application.auth;

import com.tn.donation.mc_donation.api.dto.LoginResponse;
import com.tn.donation.mc_donation.application.security.CustomUserDetails;
import com.tn.donation.mc_donation.application.security.JwtService;
import com.tn.donation.mc_donation.domain.enums.UserStatus;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.MenuEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.RoleEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    JwtService jwtService;

    @Mock
    Authentication authentication;

    @InjectMocks
    LoginService loginService;

    @Test
    void buildLoginResponse_shouldReturnTokenUserAndMenus() {
        MenuEntity menu = new MenuEntity();
        menu.setId(1L);
        menu.setOrderIndex(1);
        menu.setName("Dashboard");
        menu.setPath("/dashboard");
        menu.setIcon("iconoir:home");

        RoleEntity role = new RoleEntity(1L, "ADMIN", Set.of(menu));

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setPassword("1234");
        userEntity.setName("Elon");
        userEntity.setLastname("Musk");
        userEntity.setEmail("test@mail.com");
        userEntity.setStatus(UserStatus.PENDING);
        userEntity.setRoles(Set.of(role));

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        when(authentication.getPrincipal()).thenReturn(customUserDetails);

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.fakePayload.signature";

        when(jwtService.generateToken(customUserDetails))
                .thenReturn(token);

        LoginResponse loginResponse = loginService.buildLoginResponse(authentication);

        assertNotNull(loginResponse);
        assertEquals(1L, loginResponse.getUser().getId());
        assertEquals("test@mail.com", loginResponse.getUser().getEmail());
        assertEquals("/dashboard", loginResponse.getMenus().get(0).getPath());
        assertEquals(token, loginResponse.getToken());

        verify(authentication).getPrincipal();
        verify(jwtService).generateToken(customUserDetails);
    }
}
