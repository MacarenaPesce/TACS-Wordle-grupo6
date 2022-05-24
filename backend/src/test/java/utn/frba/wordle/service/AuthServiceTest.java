package utn.frba.wordle.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.pojo.Session;
import utn.frba.wordle.security.UserSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static utn.frba.wordle.service.AuthService.ADMIN_PASS;
import static utn.frba.wordle.service.AuthService.ADMIN_USER;

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

        Session session = authService.login(ADMIN_USER, ADMIN_PASS);

        assertNotNull(session);
    }

    @Test
    public void whenTheyTriesToLoginWithUsernameAndPasswordWeGetAnException(){
        when(userService.findUserByUsernameAndPassword(any(), any())).thenReturn(null);
        String username = "asd";
        String password = "qwe";
        authService.setJwtAccessExpiration(100L);
        authService.setJwtRefreshExpiration(200L);

        assertThrows(BusinessException.class, () -> authService.login(username, password));
    }

    @Test
    public void getValidAccessTokenWhenRefreshExpiredToken() {
        UserSession userSession = new UserSession(1L, ADMIN_USER, AuthService.ADMIN_EMAIL);

        authService.setJwtAccessExpiration(100L);
        authService.setJwtRefreshExpiration(200L);

        String token = authService.refreshAccessToken(userSession);

        assertNotNull(token);
        assertEquals("Bearer", token.substring(0, 6));
    }
}
