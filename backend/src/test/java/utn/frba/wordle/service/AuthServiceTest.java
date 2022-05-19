package utn.frba.wordle.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.dto.LoginDto;
import utn.frba.wordle.model.dto.SessionDto;
import utn.frba.wordle.security.UserSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    UserService userService;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getJWTToken() {

        authService.setJwtAccessExpiration(100L);
        authService.setJwtRefreshExpiration(200L);
        LoginDto request = LoginDto.builder()
                .email(AuthService.ADMIN_EMAIL)
                .password(AuthService.ADMIN_PASS)
                .username(AuthService.ADMIN_USER)
                .build();

        SessionDto session = authService.login(request);

        assertNotNull(session);
    }

    @Test
    public void whenTheyTriesToLoginWithUsernameAndPasswordWeGetAnException(){
        when(userService.findUserByUsernameAndPassword(any(), any())).thenReturn(null);

        assertThrows(BusinessException.class, () -> {
            LoginDto request = LoginDto.builder()
                    .email("test@test.com")
                    .password("asd123")
                    .username("test")
                    .build();
            authService.setJwtAccessExpiration(100L);
            authService.setJwtRefreshExpiration(200L);

            authService.login(request);
        });
    }

    @Test
    public void getValidAccessTokenWhenRefreshExpiredToken() {
        UserSession userSession = new UserSession(1L, AuthService.ADMIN_USER, AuthService.ADMIN_EMAIL);

        authService.setJwtAccessExpiration(100L);
        authService.setJwtRefreshExpiration(200L);

        String token = authService.refreshAccessToken(userSession);

        assertNotNull(token);
        assertEquals("Bearer", token.substring(0, 6));
    }
}
