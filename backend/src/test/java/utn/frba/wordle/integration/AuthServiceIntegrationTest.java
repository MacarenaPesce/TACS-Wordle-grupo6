package utn.frba.wordle.integration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.dto.SessionDto;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.service.AuthService;
import utn.frba.wordle.service.UserService;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static utn.frba.wordle.service.AuthService.*;


public class AuthServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @Test
    public void getsJWTOnLogin() {
        String username = "qwe";
        String password = "asd";
        String mail = "mail@mail.com";

        UserDto user = userService.createUser(username, password, mail);

        SessionDto login = authService.login(username, password);

        Claims claims = getClaims(login.getToken());
        String tokenUsername = (String) claims.get("username");
        String tokenEmail = (String) claims.get("email");
        assertThat(login).hasNoNullFieldsOrProperties();
        assertThat(tokenUsername).isEqualTo(user.getUsername());
        assertThat(tokenEmail).isEqualTo(user.getEmail());
    }

    @Test
    public void getsErrorOnLoginWithInvalidCredentials() {
        String username = "qwe";
        String password = "asd";

        assertThrows(BusinessException.class, () -> authService.login(username, password));
    }

    @Test
    public void getsJWTOnAdminsLogin() {
        SessionDto login = authService.login(ADMIN_USER, ADMIN_PASS);

        Claims claims = getClaims(login.getToken());
        String tokenUsername = (String) claims.get("username");
        String tokenEmail = (String) claims.get("email");

        assertThat(login).hasNoNullFieldsOrProperties();
        assertThat(tokenUsername).isEqualTo(ADMIN_USER);
        assertThat(tokenEmail).isEqualTo(ADMIN_EMAIL);
    }

    @Test
    public void getsJWTOnRegister() {
        String username = "usernamePrueba";
        String password = "lamePassword";
        String email = "mail@prueba.com";

        SessionDto login = authService.register(username, password, email);

        assertThat(login).hasNoNullFieldsOrProperties();
    }

    @Test
    public void whenRegisterUserWithRepeatedUsernameThrowsBusinessException() {
        String username = "usernamePrueba";
        String password = "lamePassword";
        String email = "mail@prueba.com";
        String password2 = "lamePassword2";
        String email2 = "mail@prueba.com2";

        authService.register(username, password, email);

        assertThrows(BusinessException.class, () -> authService.register(username, password2, email2));
    }

    @Test
    public void whenRegisterUserWithRepeatedEmailThrowsBusinessException() {
        String username = "usernamePrueba";
        String password = "lamePassword";
        String email = "mail@prueba.com";
        String username2 = "usernamePrueba2";
        String password2 = "lamePassword2";

        authService.register(username, password, email);

        assertThrows(BusinessException.class, () -> authService.register(username2, password2, email));
    }

    private Claims getClaims(String jwtToken) {
        jwtToken = jwtToken.replace("Bearer", "");
        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
    }
}

