package utn.frba.wordle.integration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.model.dto.LoginDto;
import utn.frba.wordle.model.dto.SessionDto;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.service.AuthService;
import utn.frba.wordle.service.UserService;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static utn.frba.wordle.service.AuthService.*;
import static utn.frba.wordle.utils.TestUtils.RANDOM;


public class AuthServiceIntegrationTest extends AbstractIntegrationTest {

    //protected static final String USUARIO_ADMIN = AuthService.USUARIO_DEFAULT_HARDCODEADO;
    //protected static final String PASS_ADMIN = AuthService.PASS_HARDCODEADA;

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @Test
    public void getsJWTOnLogin() {
        LoginDto loginDto = RANDOM.nextObject(LoginDto.class);
        UserDto user = userService.createUser(loginDto);

        SessionDto login = authService.login(loginDto);

        Claims claims = getClaims(login.getToken());
        String tokenUsername = (String) claims.get("username");
        String tokenEmail = (String) claims.get("email");
        assertThat(login).hasNoNullFieldsOrProperties();
        assertThat(tokenUsername).isEqualTo(user.getUsername());
        assertThat(tokenEmail).isEqualTo(user.getEmail());
    }

    @Test
    public void getsErrorOnLoginWithInvalidCredentials() {
        LoginDto wrongLogin = LoginDto.builder()
                .email("falseEmail@false.com")
                .password("falsePassword")
                .username("notRealUser")
                .build();

        assertThrows(BusinessException.class, () -> authService.login(wrongLogin));
    }

    @Test
    public void getsJWTOnAdminsLogin() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(ADMIN_EMAIL);
        loginDto.setUsername(ADMIN_USER);
        loginDto.setPassword(ADMIN_PASS);

        SessionDto login = authService.login(loginDto);

        Claims claims = getClaims(login.getToken());
        String tokenUsername = (String) claims.get("username");
        String tokenEmail = (String) claims.get("email");

        assertThat(login).hasNoNullFieldsOrProperties();
        assertThat(tokenUsername).isEqualTo(ADMIN_USER);
        assertThat(tokenEmail).isEqualTo(ADMIN_EMAIL);
    }

    @Test
    public void getsJWTOnRegister() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("mail@prueba.com");
        loginDto.setUsername("usernamePrueba");
        loginDto.setPassword("lamePassword");

        SessionDto login = authService.register(loginDto);

        assertThat(login).hasNoNullFieldsOrProperties();
    }

    @Test
    public void whenRegisterUserWithRepeatedUsernameThrowsBusinessException() {
        LoginDto loginDto = LoginDto.builder()
                    .email("mail@prueba.com")
                    .username("username")
                    .password("lamePassword")
                    .build();

        authService.register(loginDto);

        LoginDto loginDto2 = LoginDto.builder()
                .email("mail2@prueba2.com")
                .username("username")
                .password("lamePassword123")
                .build();

        assertThrows(BusinessException.class, () -> authService.register(loginDto2));
    }

    @Test
    public void whenRegisterUserWithRepeatedEmailThrowsBusinessException() {
        LoginDto loginDto = LoginDto.builder()
                .email("mail@prueba.com")
                .username("username1")
                .password("lamePassword")
                .build();

        authService.register(loginDto);

        LoginDto loginDto2 = LoginDto.builder()
                .email("mail@prueba.com")
                .username("username2")
                .password("lamePassword123")
                .build();

        assertThrows(BusinessException.class, () -> authService.register(loginDto2));
    }

    private Claims getClaims(String jwtToken) {
        jwtToken = jwtToken.replace("Bearer", "");
        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
    }
}

