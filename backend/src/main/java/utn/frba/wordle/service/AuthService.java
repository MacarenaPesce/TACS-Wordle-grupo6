package utn.frba.wordle.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.LoginDto;
import utn.frba.wordle.dto.SessionDto;
import utn.frba.wordle.dto.UserDto;
import utn.frba.wordle.entity.UserEntity;

import java.util.Date;

@Service
@NoArgsConstructor
public class AuthService {

    public static String ADMIN_USER = "utn";
    public static String ADMIN_PASS = "utn";
    public static String ADMIN_EMAIL = "utnfrba@admin.com";

    public static final String SECRET = "aVeryRandomSecretAndGreenKey";

    @Value("${jwt.access.expiration}")
    private Long jwtAccessExpiration;

    @Autowired
    UserService userService;

    public SessionDto register(LoginDto loginDto) {
        UserDto userDto = userService.createUser(loginDto);
        return getSessionDto(userDto);
    }

    public SessionDto login(LoginDto loginDto) {
        if (loginDto.getUsername().equals(ADMIN_USER) &&
                loginDto.getPassword().equals(ADMIN_PASS)) {
            return getSessionDtoHardcodeado(loginDto);
        } else {
            UserEntity userEntity = userService.findUserByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword());
            /*if (userEntity == null) {
                throw new BusinessException("Usuario inválido");
            }*/
            //userService.updateLoginDate(userEntity);
            return getSessionDto(userEntity);
        }
    }


    private SessionDto getSessionDto(UserDto userDto) {
        String accessToken = getJWTToken(userDto.getUsername(), userDto.getEmail(), userDto.getId(), jwtAccessExpiration);

        return SessionDto.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .userId(userDto.getId())
                .token(accessToken)
                .build();
    }

    private SessionDto getSessionDto(UserEntity userEntity) {
        return getSessionDto(UserService.mapToDto(userEntity));
    }

    private SessionDto getSessionDtoHardcodeado(LoginDto loginDto) {
        String accessToken = getJWTToken(ADMIN_USER, ADMIN_EMAIL, 0L, jwtAccessExpiration);

        return SessionDto.builder()
                .token(accessToken)
                .build();
    }

    public String getJWTToken(String username, String email, Long userId, Long jwtExpiration) {

        String token = Jwts
                .builder()
                .setId("WordleJWT")
                .setSubject(username)
                .claim("idUsuario", userId)
                .claim("usuario", username)
                .claim("email", email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000))
                .signWith(SignatureAlgorithm.HS512,
                        SECRET.getBytes()).compact();

        return "Bearer " + token;
    }
}
