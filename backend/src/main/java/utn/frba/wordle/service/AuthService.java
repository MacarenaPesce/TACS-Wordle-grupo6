package utn.frba.wordle.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.LoginDto;
import utn.frba.wordle.dto.SessionDto;

import java.util.Date;

@Service
@NoArgsConstructor
public class AuthService {

    public static String USUARIO_DEFAULT_HARDCODEADO = "utn";
    public static String EMAIL_HARDCODEADO = "utnfrba@admin.com";

    @Value("${jwt.access.expiration}")
    private Long jwtAccessExpiration;

    public SessionDto register(LoginDto loginDto) {
        return getSessionDtoHardcodeado(loginDto);
    }

    public SessionDto login(LoginDto loginDto) {
        // dummy login
        return getSessionDtoHardcodeado(loginDto);

        /*if (loginDto.getUsername().equals(USUARIO_DEFAULT_HARDCODEADO) &&
                loginDto.getPassword().equals(PASS_HARDCODEADA)) {
            return getSessionDtoHardcodeado(loginDto);
        } else {
            UsuarioEntity usuarioEntity = usuarioService.buscarUsuarioPorUsernameYPassword(loginDto.getUsername(), loginDto.getPassword());
            if (usuarioEntity == null) {
                throw new BusinessException("Usuario inválido");
            }
            usuarioService.actualizarFechaLogin(usuarioEntity);
            return getSessionDto(usuarioEntity);
        }*/
    }

    private SessionDto getSessionDtoHardcodeado(LoginDto loginDto) {
        //String accessToken = getJWTToken(USUARIO_DEFAULT_HARDCODEADO, EMAIL_HARDCODEADO, jwtAccessExpiration);
        String accessToken = getJWTToken(loginDto.getUsername(), loginDto.getPassword(), jwtAccessExpiration);

        return SessionDto.builder()
                .token(accessToken)
                .build();
    }
    public String getJWTToken(String username, String email, Long jwtExpiration) {

        String secretKey = "mySecretKey";

        String token = Jwts
                .builder()
                .setId("WordleJWT")
                .setSubject(username)
                .claim("usuario", username)
                .claim("email", email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }
}
