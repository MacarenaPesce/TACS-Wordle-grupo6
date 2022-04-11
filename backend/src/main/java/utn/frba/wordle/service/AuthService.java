package utn.frba.wordle.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.LoginDto;
import utn.frba.wordle.dto.SessionDto;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.UsuarioEntity;
import utn.frba.wordle.security.SessionUser;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class AuthService {

    public static String USUARIO_DEFAULT_HARDCODEADO = "utn";
    public static String EMAIL_HARDCODEADO = "utnfrba@admin.com";

    @Autowired
    UsuarioService usuarioService;

    @Value("${jwt.access.expiration}")
    private Long jwtAccessExpiration;

    public SessionDto login(LoginDto loginDto) throws BusinessException {
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

    private SessionDto getSessionDto(UsuarioEntity usuarioEntity) {
        String accessToken = getJWTToken(usuarioEntity.getUsuario(), usuarioEntity.getEmail(), jwtAccessExpiration);

        return SessionDto.builder()
                .token(accessToken)
                .build();
    }

    private SessionDto getSessionDtoHardcodeado(LoginDto loginDto) {
        String accessToken = getJWTToken(USUARIO_DEFAULT_HARDCODEADO, EMAIL_HARDCODEADO, jwtAccessExpiration);

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
