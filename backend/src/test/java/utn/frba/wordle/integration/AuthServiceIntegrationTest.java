package utn.frba.wordle.integration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.dto.LoginDto;
import utn.frba.wordle.dto.SessionDto;
import utn.frba.wordle.dto.UserDto;
import utn.frba.wordle.service.AuthService;
import utn.frba.wordle.service.UserService;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
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
        String tokenUsername = (String) claims.get("usuario");
        String tokenEmail = (String) claims.get("email");

        assertThat(login).hasNoNullFieldsOrProperties();
        assertThat(tokenUsername).isEqualTo(user.getUsername());
        assertThat(tokenEmail).isEqualTo(user.getEmail());
    }

    @Test
    public void getsJWTOnAdminsLogin() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(ADMIN_EMAIL);
        loginDto.setUsername(ADMIN_USER);
        loginDto.setPassword(ADMIN_PASS);

        SessionDto login = authService.login(loginDto);

        Claims claims = getClaims(login.getToken());
        String tokenUsername = (String) claims.get("usuario");
        String tokenEmail = (String) claims.get("email");

        assertThat(login).hasNoNullFieldsOrProperties();
        assertThat(tokenUsername).isEqualTo(ADMIN_USER);
        assertThat(tokenEmail).isEqualTo(ADMIN_EMAIL);

    }

    private Claims getClaims(String jwtToken) {
        jwtToken = jwtToken.replace("Bearer", "");
        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
    }
/*
    @Test
    public void alRealizarElLoginConElAdminDeboPoderLoguearme() {
        LoginDto loginDto = RANDOM.nextObject(LoginDto.class);
        loginDto.setUsername(USUARIO_ADMIN);
        loginDto.setPassword(PASS_ADMIN);

        SessionDto login = authService.login(loginDto);

        assertThat(login).hasNoNullFieldsOrProperties();
    }

    @Test
    public void alRealizarElLoginConUnUsuarioNoAdminDeboPoderLoguearme() {
        UsuarioDto usuarioDto = RANDOM.nextObject(UsuarioDto.class);
        usuarioDto.setIdsGrupos(null);
        usuarioDto.setEmail(TestUtils.MAIL_TESTING);
        usuarioDto = usuarioService.create(usuarioDto);
        String password = usuarioService.generarPasswordDefault(usuarioDto.getLegajo());
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(usuarioDto.getUsuario());
        loginDto.setPassword(password);

        SessionDto login = authService.login(loginDto);

        assertThat(login).hasNoNullFieldsOrProperties();
    }

    @Test()
    public void alRealizarElLoginConUnUsuarioNoValidoObtengoUnError() {
        assertThrows(BusinessException.class, () -> {
            LoginDto loginDto = RANDOM.nextObject(LoginDto.class);

            authService.login(loginDto);
        });
    }
    */
}

