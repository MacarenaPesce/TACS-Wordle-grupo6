package utn.frba.wordle.service;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.dto.Session;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.entity.UserEntity;
import utn.frba.wordle.security.UserSession;

import java.util.Base64;
import java.util.Date;

@Service
@NoArgsConstructor
public class AuthService {

    public static final String ADMIN_USER = "utn";
    public static final String ADMIN_PASS = "utn";
    public static final String ADMIN_EMAIL = "utnfrba@admin.com";

    public static final String SECRET = "aVeryRandomSecretAndGreenKey";

    @Value("${jwt.access.expiration}")
    private Long jwtAccessExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long jwtRefreshExpiration;

    @Autowired
    UserService userService;

    public static Session getSession(String token) {
        token = token.replace("Bearer", "");

        String[] chunks = token.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));

        return new Gson().fromJson(payload, Session.class);
    }

    public Session register(String username, String password, String email) {
        UserEntity userEntity;

        userEntity = userService.getUserByUsername(username.toLowerCase());
        if(userEntity != null){
            throw new BusinessException(String.format("El usuario %s ya se encuentra registrado", username));
        }

        userEntity = userService.findUserByEmail(email.toLowerCase());
        if(userEntity != null){
            throw new BusinessException("El mail ingresado ya se ecuentra en uso");
        }

        UserDto userDto = userService.createUser(username, password, email);
        return getSessionDto(userDto);
    }

    public Session login(String username, String password) {
        if (username.equals(ADMIN_USER) &&
                password.equals(ADMIN_PASS)) {
            return getSessionDtoHardcodeado();
        } else {
            UserEntity userEntity = userService.findUserByUsernameAndPassword(username.toLowerCase(), password);
            if (userEntity == null) {
                throw new BusinessException("Combinación de usuario y contraseña inválidos");
            }
            //userService.updateLoginDate(userEntity);
            return getSessionDto(userEntity);
        }
    }


    private Session getSessionDto(UserDto userDto) {
        String accessToken = getJWTToken(userDto.getUsername(), userDto.getEmail(), userDto.getId(), jwtAccessExpiration);

        return Session.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .userId(userDto.getId())
                .token(accessToken)
                .build();
    }

    private Session getSessionDto(UserEntity userEntity) {
        return getSessionDto(UserService.mapToDto(userEntity));
    }

    private Session getSessionDtoHardcodeado() {
        String accessToken = getJWTToken(ADMIN_USER, ADMIN_EMAIL, 0L, jwtAccessExpiration);

        return Session.builder()
                .token(accessToken)
                .username(ADMIN_USER)
                .email(ADMIN_EMAIL)
                .userId(0L)
                .build();
    }

    public String getJWTToken(String username, String email, Long userId, Long jwtExpiration) {

        String token = Jwts
                .builder()
                .setId("WordleJWT")
                .setSubject(username)
                .claim("userId", userId)
                .claim("username", username)
                .claim("email", email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000))
                .signWith(SignatureAlgorithm.HS512,
                        SECRET.getBytes()).compact();

        return "Bearer " + token;
    }

    public static Claims getClaims(String jwtToken) {
        jwtToken = jwtToken.replace("Bearer", "");
        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
    }

    public void setJwtAccessExpiration(Long jwtAccessExpiration) {
        this.jwtAccessExpiration = jwtAccessExpiration;
    }

    public void setJwtRefreshExpiration(Long jwtRefreshExpiration) {
        this.jwtRefreshExpiration = jwtRefreshExpiration;
    }

    public String refreshAccessToken(UserSession userSession) {
        return getJWTToken(userSession.getUsername(), userSession.getEmail(), userSession.getUserId(), jwtAccessExpiration);

    }
}
